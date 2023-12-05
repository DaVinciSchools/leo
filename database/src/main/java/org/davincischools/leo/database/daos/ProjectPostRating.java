package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.davincischools.leo.database.dao_interfaces.PropagateDeleteFrom;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity(name = ProjectPostRating.ENTITY_NAME)
@Table(name = ProjectPostRating.TABLE_NAME, schema = "leo_test")
public class ProjectPostRating implements Serializable {

  public static final String ENTITY_NAME = "ProjectPostRating";
  public static final String TABLE_NAME = "project_post_rating";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_RATING_NAME = "rating";
  public static final String COLUMN_RATINGTYPE_NAME = "rating_type";
  public static final String COLUMN_LONGDESCRHTML_NAME = "long_descr_html";
  public static final String COLUMN_GOALPROGRESS_NAME = "goal_progress";
  public static final String COLUMN_GOALREMAINING_NAME = "goal_remaining";
  private static final long serialVersionUID = 1616415498133771364L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private Integer rating;

  private String ratingType;

  private String longDescrHtml;

  private String goalProgress;

  private String goalRemaining;

  private UserX userX;

  private ProjectPost projectPost;

  private KnowledgeAndSkill knowledgeAndSkill;

  private ProjectInputFulfillment projectInputFulfillment;

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

  @Column(name = COLUMN_RATING_NAME, nullable = false)
  public Integer getRating() {
    return rating;
  }

  @Lob
  @Column(name = COLUMN_RATINGTYPE_NAME, nullable = false)
  public String getRatingType() {
    return ratingType;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCRHTML_NAME)
  public String getLongDescrHtml() {
    return longDescrHtml;
  }

  @Lob
  @Column(name = COLUMN_GOALPROGRESS_NAME)
  public String getGoalProgress() {
    return goalProgress;
  }

  @Lob
  @Column(name = COLUMN_GOALREMAINING_NAME)
  public String getGoalRemaining() {
    return goalRemaining;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "knowledge_and_skill_id")
  @PropagateDeleteFrom
  public KnowledgeAndSkill getKnowledgeAndSkill() {
    return knowledgeAndSkill;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_input_fulfillment_id")
  @PropagateDeleteFrom
  public ProjectInputFulfillment getProjectInputFulfillment() {
    return projectInputFulfillment;
  }
}
