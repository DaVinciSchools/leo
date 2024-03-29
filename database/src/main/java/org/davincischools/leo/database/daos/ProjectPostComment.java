package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.davincischools.leo.database.dao_interfaces.PropagateDeleteFrom;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity(name = ProjectPostComment.ENTITY_NAME)
@Table(
    name = ProjectPostComment.TABLE_NAME,
    schema = "leo_test",
    indexes = {
      @Index(name = "creation_time", columnList = "creation_time"),
      @Index(name = "post_time", columnList = "post_time"),
      @Index(name = "being_edited", columnList = "being_edited")
    })
public class ProjectPostComment implements Serializable {

  public static final String ENTITY_NAME = "ProjectPostComment";
  public static final String TABLE_NAME = "project_post_comment";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_POSTTIME_NAME = "post_time";
  public static final String COLUMN_COMMENTHTML_NAME = "comment_html";
  public static final String COLUMN_LONGDESCRHTML_NAME = "long_descr_html";
  public static final String COLUMN_BEINGEDITED_NAME = "being_edited";
  public static final String COLUMN_POSITIVEFEEDBACK_NAME = "positive_feedback";
  public static final String COLUMN_HOWIMPROVEDFEEDBACK_NAME = "how_improved_feedback";
  public static final String COLUMN_TOIMPROVEFEEDBACK_NAME = "to_improve_feedback";
  public static final String COLUMN_HASENOUGHCONTENTPERCENT_NAME = "has_enough_content_percent";
  public static final String COLUMN_INCREMENTALPOSTSUMMARY_NAME = "incremental_post_summary";
  public static final String COLUMN_FEEDBACKRESPONSEHTML_NAME = "feedback_response_html";
  public static final String COLUMN_AIPROMPT_NAME = "ai_prompt";
  public static final String COLUMN_AIRESPONSE_NAME = "ai_response";
  @Serial private static final long serialVersionUID = 8300945491055191413L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private Instant postTime;

  private String commentHtml;

  private String longDescrHtml;

  private Boolean beingEdited;

  private String positiveFeedback;

  private String howImprovedFeedback;

  private String toImproveFeedback;

  private Integer hasEnoughContentPercent;

  private String incrementalPostSummary;

  private String feedbackResponseHtml;

  private String aiPrompt;

  private String aiResponse;

  private UserX userX;

  private ProjectPost projectPost;

  private Set<Tag> tags = new LinkedHashSet<>();

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  @Column(name = COLUMN_POSTTIME_NAME, nullable = false)
  public Instant getPostTime() {
    return postTime;
  }

  @Lob
  @Column(name = COLUMN_COMMENTHTML_NAME)
  public String getCommentHtml() {
    return commentHtml;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCRHTML_NAME)
  public String getLongDescrHtml() {
    return longDescrHtml;
  }

  @Column(name = COLUMN_BEINGEDITED_NAME)
  public Boolean getBeingEdited() {
    return beingEdited;
  }

  @Lob
  @Column(name = COLUMN_POSITIVEFEEDBACK_NAME)
  public String getPositiveFeedback() {
    return positiveFeedback;
  }

  @Lob
  @Column(name = COLUMN_HOWIMPROVEDFEEDBACK_NAME)
  public String getHowImprovedFeedback() {
    return howImprovedFeedback;
  }

  @Lob
  @Column(name = COLUMN_TOIMPROVEFEEDBACK_NAME)
  public String getToImproveFeedback() {
    return toImproveFeedback;
  }

  @Column(name = COLUMN_HASENOUGHCONTENTPERCENT_NAME)
  public Integer getHasEnoughContentPercent() {
    return hasEnoughContentPercent;
  }

  @Lob
  @Column(name = COLUMN_INCREMENTALPOSTSUMMARY_NAME)
  public String getIncrementalPostSummary() {
    return incrementalPostSummary;
  }

  @Lob
  @Column(name = COLUMN_FEEDBACKRESPONSEHTML_NAME)
  public String getFeedbackResponseHtml() {
    return feedbackResponseHtml;
  }

  @Lob
  @Column(name = COLUMN_AIPROMPT_NAME)
  public String getAiPrompt() {
    return aiPrompt;
  }

  @Lob
  @Column(name = COLUMN_AIRESPONSE_NAME)
  public String getAiResponse() {
    return aiResponse;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_x_id", nullable = false)
  @PropagateDeleteFrom
  public UserX getUserX() {
    return userX;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_post_id", nullable = false)
  @PropagateDeleteFrom
  public ProjectPost getProjectPost() {
    return projectPost;
  }

  @OneToMany(mappedBy = "projectPostComment")
  public Set<Tag> getTags() {
    return tags;
  }
}
