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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Entity(name = ProjectPost.ENTITY_NAME)
@Table(name = ProjectPost.TABLE_NAME, schema = "leo_test")
public class ProjectPost implements Serializable {

  public static final String ENTITY_NAME = "ProjectPost";
  public static final String TABLE_NAME = "project_post";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_MESSAGEHTML_NAME = "message_html";
  public static final String COLUMN_POSTTIME_NAME = "post_time";
  public static final String COLUMN_LONGDESCRHTML_NAME = "long_descr_html";
  public static final String COLUMN_DESIREDFEEDBACK_NAME = "desired_feedback";
  public static final String COLUMN_BEINGEDITED_NAME = "being_edited";
  private static final long serialVersionUID = 6660237520132538098L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String name;

  private String messageHtml;

  private Instant postTime;

  private String longDescrHtml;

  private String desiredFeedback;

  private Boolean beingEdited;

  private UserX userX;

  private Project project;

  private Set<ProjectPostComment> projectPostComments = new LinkedHashSet<>();

  private Set<ProjectPostRating> projectPostRatings = new LinkedHashSet<>();

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

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  @Lob
  @Column(name = COLUMN_MESSAGEHTML_NAME)
  public String getMessageHtml() {
    return messageHtml;
  }

  @Column(name = COLUMN_POSTTIME_NAME, nullable = false)
  public Instant getPostTime() {
    return postTime;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCRHTML_NAME)
  public String getLongDescrHtml() {
    return longDescrHtml;
  }

  @Lob
  @Column(name = COLUMN_DESIREDFEEDBACK_NAME)
  public String getDesiredFeedback() {
    return desiredFeedback;
  }

  @Column(name = COLUMN_BEINGEDITED_NAME)
  public Boolean getBeingEdited() {
    return beingEdited;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_x_id", nullable = false)
  @PropagateDeleteFrom
  public UserX getUserX() {
    return userX;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", nullable = false)
  @PropagateDeleteFrom
  public Project getProject() {
    return project;
  }

  @OneToMany(mappedBy = "projectPost")
  public Set<ProjectPostComment> getProjectPostComments() {
    return projectPostComments;
  }

  @OneToMany(mappedBy = "projectPost")
  public Set<ProjectPostRating> getProjectPostRatings() {
    return projectPostRatings;
  }

  @OneToMany(mappedBy = "projectPost")
  public Set<Tag> getTags() {
    return tags;
  }
}
