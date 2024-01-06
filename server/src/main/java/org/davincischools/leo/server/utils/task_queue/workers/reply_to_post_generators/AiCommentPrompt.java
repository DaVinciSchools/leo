package org.davincischools.leo.server.utils.task_queue.workers.reply_to_post_generators;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AiCommentPrompt {

  @Getter
  @Setter
  @Accessors(chain = true)
  public static class Goal {
    public int goalIdNumber;
    public String howTheProjectFulfillsThisGoal;
    public String whatToLookForToShowCompletionOfThisGoal;
  }

  // Project information.
  public String projectSummary;
  public List<Goal> goals = new ArrayList<>();

  // Post information.
  public String newPostContent;
  public String newPostFeedbackRequest;

  public String previousPostsSummary;
  public String previousPositiveFeedback;
  public String previousThingsToImproveFeedback;
  public String previousHowImprovedFeedback;
  public String previousFeedbackResponses;
}
