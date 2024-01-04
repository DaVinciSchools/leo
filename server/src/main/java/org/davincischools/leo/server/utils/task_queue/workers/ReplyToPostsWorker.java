package org.davincischools.leo.server.utils.task_queue.workers;

import static java.util.Comparator.comparing;
import static org.davincischools.leo.database.utils.DaoUtils.listIfInitialized;
import static org.davincischools.leo.database.utils.DaoUtils.streamIfInitialized;
import static org.davincischools.leo.server.utils.HtmlUtils.stripOutHtml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectInputFulfillment;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.daos.ProjectPostComment;
import org.davincischools.leo.database.daos.ProjectPostRating;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.GetProjectInputsParams;
import org.davincischools.leo.database.utils.repos.GetProjectPostsParams;
import org.davincischools.leo.database.utils.repos.GetProjectsParams;
import org.davincischools.leo.database.utils.repos.ProjectPostRatingRepository.RatingType;
import org.davincischools.leo.protos.task_service.ReplyToPostTask;
import org.davincischools.leo.server.utils.task_queue.DefaultTaskMetadata;
import org.davincischools.leo.server.utils.task_queue.TaskQueue;
import org.davincischools.leo.server.utils.task_queue.workers.reply_to_post_generators.AiComment;
import org.davincischools.leo.server.utils.task_queue.workers.reply_to_post_generators.AiCommentGenerator;
import org.davincischools.leo.server.utils.task_queue.workers.reply_to_post_generators.AiCommentPrompt;
import org.davincischools.leo.server.utils.task_queue.workers.reply_to_post_generators.AiCommentPrompt.Goal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class ReplyToPostsWorker extends TaskQueue<ReplyToPostTask, DefaultTaskMetadata> {

  private static final Logger logger = LogManager.getLogger();
  private static final ObjectMapper jsonObjectMapper =
      new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

  private final Database db;
  private final AiCommentGenerator aiCommentGenerator;

  public ReplyToPostsWorker(
      @Autowired Database db, @Autowired AiCommentGenerator aiCommentGenerator) {
    super(10);
    this.db = db;
    this.aiCommentGenerator = aiCommentGenerator;
  }

  @Override
  protected DefaultTaskMetadata createDefaultMetadata() {
    return new DefaultTaskMetadata().setRetries(2);
  }

  @Override
  protected void scanForTasks() {
    db.getProjectRepository()
        .getProjects(
            new GetProjectsParams()
                .setIncludeInputs(new GetProjectInputsParams().setIncludeComplete(true)))
        .forEach(p -> submitTask(ReplyToPostTask.newBuilder().setProjectId(p.getId()).build()));
  }

  @Override
  protected boolean processTask(ReplyToPostTask task, DefaultTaskMetadata metadata)
      throws IOException {

    Project project =
        Iterables.getOnlyElement(
            db.getProjectRepository()
                .getProjects(
                    new GetProjectsParams()
                        .setProjectIds(List.of(task.getProjectId()))
                        .setIncludeInactive(true)
                        .setIncludeFulfillments(true)
                        .setIncludeProjectPosts(
                            new GetProjectPostsParams()
                                .setBeingEdited(false)
                                .setIncludeComments(true))),
            null);
    if (project == null || listIfInitialized(project.getProjectPosts()).isEmpty()) {
      return false;
    }

    AiCommentPrompt promptContent = new AiCommentPrompt();

    // Add project information.

    promptContent.setProjectSummary(
        Strings.nullToEmpty(project.getName())
            + ": "
            + Strings.nullToEmpty(project.getShortDescr())
            + " "
            + stripOutHtml(Strings.nullToEmpty(project.getLongDescrHtml())));
    listIfInitialized(project.getProjectInputFulfillments())
        .forEach(
            fulfillment -> {
              Goal goal = new Goal();
              goal.setGoalIdNumber(fulfillment.getId());
              goal.setHowTheProjectFulfillsThisGoal(fulfillment.getHowProjectFulfills());
              goal.setWhatToLookForToShowCompletionOfThisGoal(fulfillment.getVisibleIndicator());
              promptContent.getGoals().add(goal);
            });

    // Step through posts.

    // These will accumulate information that will be used to generate each post's comment.
    StringBuilder previousPostsSummary = new StringBuilder();
    StringBuilder previousPositiveFeedback = new StringBuilder();
    StringBuilder previousToImproveFeedback = new StringBuilder();
    StringBuilder previousHowImprovedFeedback = new StringBuilder();
    StringBuilder previousFeedbackResponses = new StringBuilder();

    // Step through posts and build up a query for each post.
    for (var projectPost :
        streamIfInitialized(project.getProjectPosts())
            .sorted(comparing(ProjectPost::getId))
            .toList()) {

      // Get the most recent Coach Leo comment for that post, if it exists.
      Optional<ProjectPostComment> coachComment =
          streamIfInitialized(projectPost.getProjectPostComments())
              .filter(
                  c ->
                      Objects.equals(
                          c.getUserX().getId(),
                          db.getUserXRepository().getProjectLeoCoach().getId()))
              .max(comparing(ProjectPostComment::getId));

      // If the Coach Leo comment doesn't exist yet, create one.
      if (coachComment.isEmpty()) {
        promptContent.setNewPostContent(
            stripOutHtml(Strings.nullToEmpty(projectPost.getLongDescrHtml()).trim()));
        promptContent.setNewPostFeedbackRequest(
            stripOutHtml(Strings.nullToEmpty(projectPost.getDesiredFeedback()).trim()));

        promptContent.setPreviousPostsSummary(
            Strings.nullToEmpty(previousPostsSummary.toString().trim()));
        promptContent.setPreviousPositiveFeedback(
            Strings.nullToEmpty(previousPositiveFeedback.toString().trim()));
        promptContent.setPreviousToImproveFeedback(
            Strings.nullToEmpty(previousToImproveFeedback.toString().trim()));
        promptContent.setPreviousHowImprovedFeedback(
            Strings.nullToEmpty(previousHowImprovedFeedback.toString().trim()));
        promptContent.setPreviousFeedbackResponses(
            Strings.nullToEmpty(previousFeedbackResponses.toString().trim()));

        AiComment aiComment = aiCommentGenerator.generateComment(promptContent);
        logger.atInfo().log(
            "AI Comment Response: {}", jsonObjectMapper.writeValueAsString(aiComment));

        // Convert the AI comment into a Dao comment.
        ProjectPostComment newComment =
            new ProjectPostComment()
                .setCreationTime(Instant.now())
                .setUserX(db.getUserXRepository().getProjectLeoCoach())
                .setPostTime(Instant.now())
                .setLongDescrHtml(aiComment.getFeedbackSummary())
                .setIncrementalPostSummary(aiComment.getNewContentSummary())
                .setPositiveFeedback(aiComment.getPositiveFeedback())
                .setToImproveFeedback(aiComment.getWhatToImproveFeedback())
                .setHowImprovedFeedback(aiComment.getHowTheyImprovedFeedback())
                .setFeedbackResponseHtml(aiComment.getFeedbackRequestResponse())
                .setHasEnoughContentPercent(aiComment.getHasEnoughContent())
                .setProjectPost(projectPost);
        db.getProjectPostCommentRepository().save(newComment);

        // Save the goal ratings.
        db.getProjectPostRatingRepository()
            .saveAll(
                aiComment.getGoalRatings().stream()
                    .map(
                        g ->
                            new ProjectPostRating()
                                .setCreationTime(Instant.now())
                                .setUserX(db.getUserXRepository().getProjectLeoCoach())
                                .setProjectPost(projectPost)
                                .setProjectInputFulfillment(
                                    new ProjectInputFulfillment().setId(g.goalIdNumber))
                                .setRating(g.goalProgressScore)
                                .setRatingType(RatingType.GOAL_COMPLETE_PCT.name())
                                .setGoalProgress(g.goalProgressSummary)
                                .setGoalRemaining(g.goalRemainingSummary))
                    .toList());

        coachComment = Optional.of(newComment);
      }

      // Update the previous post summaries for use with the next post.
      previousPostsSummary.append(" ").append(coachComment.get().getIncrementalPostSummary());
      previousPositiveFeedback.append(" ").append(coachComment.get().getPositiveFeedback());
      previousToImproveFeedback.append(" ").append(coachComment.get().getToImproveFeedback());
      previousHowImprovedFeedback.append(" ").append(coachComment.get().getHowImprovedFeedback());
      previousFeedbackResponses.append(" ").append(coachComment.get().getFeedbackResponseHtml());
    }

    return true;
  }
}
