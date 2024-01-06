package org.davincischools.leo.server.utils.task_queue.workers.reply_to_post_generators;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiComment {

  public static class GoalRating {
    @JsonPropertyDescription(
        "This is a required field. It must have the id of the goal being evaluated." //
            + " This is the unique goal id for the goal that is being evaluated.")
    @JsonProperty(required = true)
    public int goalIdNumber;

    @JsonPropertyDescription(
        "This is a required field. It must be a value from 0 to 100." //
            + " Provide a score from 0 to 100 indicating how much of the goal the student has"
            + " completed." //
            + " Be sure to consider the content in both the [new_post_content] and"
            + " the [previous_posts_summaries] when determining what has been completed.")
    @JsonProperty(required = true)
    public int goalProgressScore;

    @JsonPropertyDescription(
        "This is a required field, it must be a non-empty string." //
            + " Tell another teacher, in plain text, what the student has done so far to complete"
            + " this goal." //
            + " Be sure to consider the content in both the [new_post_content] and"
            + " the [previous_posts_summaries] when determining what has been completed.")
    @JsonProperty(required = true)
    public String goalProgressSummary;

    @JsonPropertyDescription(
        "This is a required field, it must be a non-empty string." //
            + " Tell another teacher, in plain text, what the student still needs to do to"
            + " complete this goal." //
            + " Be sure to consider the content in both the [new_post_content] and"
            + " the [previous_posts_summaries] when determining what has been completed.")
    @JsonProperty(required = true)
    public String goalRemainingSummary;
  }

  @JsonPropertyDescription(
      "This is a required field. It must contain an entry for every goal in the prompt." //
          + " Every goal in the prompt must have a corresponding entry in this list."
          + " Be sure to include each goal id.")
  @JsonProperty(required = true)
  public List<GoalRating> goalRatings;

  @JsonPropertyDescription(
      "This is a required field, it must be a non-empty string." //
          + " State, in the past tense, in plain text, what's new in the [new_post_content]"
          + " compared to the content in the [previous_posts_summaries]."
          + " It should include information about new progress towards completing the goals"
          + " outlined in the prompt.")
  @JsonProperty(required = true)
  public String newContentSummary;

  @JsonPropertyDescription(
      "This is a required field, it must be a non-empty string." //
          + " Tell the student what they have done well in their [new_post_content]."
          + " This should be a positive statement that encourages the student to continue"
          + " learning." //
          + " It must be different from all previously created positive feedback in"
          + " [previous_positive_feedback].")
  @JsonProperty(required = true)
  public String positiveFeedback;

  @JsonPropertyDescription(
      "This is a required field, it must be a non-empty string." //
          + " Tell the student how [new_post_content] has improved by incorporating previously"
          + " suggested feedback in [previous_things_to_improve_feedback]."
          + " Tell the student what the suggestion was in [previous_things_to_improve_feedback]"
          + " and specifically what they did in [new_post_content] that shows improvement."
          + " If the student did not show improvement based on previous suggestions in"
          + " [previous_things_to_improve_feedback], then this field should be empty.")
  @JsonProperty(required = true)
  public String howTheyImprovedFeedback;

  @JsonPropertyDescription(
      "This is a required field, it must be a non-empty string." //
          + " Tell the student what they could do to the [new_post_content] to make it even"
          + " better or that would make your feedback better!" //
          + " The suggestion should be stated in a positive way that would encourage a student"
          + " who is struggling or who has a hard time taking feedback."
          + " It must be different from all previously suggested improvements in the"
          + " [previous_things_to_improve_feedback].")
  @JsonProperty(required = true)
  public String whatToImproveFeedback;

  @JsonPropertyDescription(
      "This is a required field, it must be a non-empty string." //
          + " Tell the student, in plain text, the answers to their questions that are in"
          + " [new_post_questions]. The answers must be different from all the other answers in"
          + " [previous_new_post_question_responses]. Be sure to indicate that you are answering"
          + " their questions in this response.")
  @JsonProperty(required = true)
  public String feedbackRequestResponse;

  @JsonPropertyDescription(
      "This is a required field, it must be a value from 0 to 100." //
          + " Provide a score from 0 to 100 indicating whether the [new_post_content] has"
          + " enough information for you to provide useful feedback about completing the goals"
          + " in the prompt." //
          + " If the [new_post_content] does not have enough information, add a suggestion to"
          + " the 'whatToImproveFeedback' parameter for this function of what types of"
          + " information would have been useful to add to the [new_post_content].")
  @JsonProperty(required = true)
  public int hasEnoughContent;

  @JsonPropertyDescription(
      "This is a required field, it must be a non-empty string." //
          + " Tell the student, in HTML, a summary of the information in the 'positiveFeedback',"
          + " 'whatToImproveFeedback', and 'howTheyImprovedFeedback' parameters for this function."
          + " This should not be a bulleted list, but one or more paragraphs of text."
          + " Then, if there is any content in the 'feedbackRequestResponse' parameter for this"
          + " function, include it as another paragraph.")
  @JsonProperty(required = true)
  public String feedbackSummary;
}
