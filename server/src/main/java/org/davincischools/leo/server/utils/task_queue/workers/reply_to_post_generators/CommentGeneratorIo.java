package org.davincischools.leo.server.utils.task_queue.workers.reply_to_post_generators;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.davincischools.leo.database.daos.ProjectPost;

@Getter
@Setter
@Accessors(chain = true)
public class CommentGeneratorIo {

  @Getter
  @Setter
  @Accessors(chain = true)
  public static class Goal {
    private int goalIdNumber;
    private String howTheProjectFulfillsThisGoal;
    private String whatToLookForToShowCompletionOfThisGoal;
  }

  private ProjectPost projectPost;

  // Project information.
  private String projectSummary;
  private List<Goal> goals = new ArrayList<>();

  // Post information.
  private String newPostContent;
  private String newPostFeedbackRequest;

  private String previousPostsSummary;
  private String previousPositiveFeedback;
  private String previousThingsToImproveFeedback;
  private String previousHowImprovedFeedback;
  private String previousFeedbackResponses;

  // Project post comment results.
  private AiComment aiComment;
  private String aiPrompt;
  private String aiResponse;
}
