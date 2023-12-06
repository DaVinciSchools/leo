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
@Entity(name = Project.ENTITY_NAME)
@Table(name = Project.TABLE_NAME, schema = "leo_test")
public class Project implements Serializable {

  public static final String ENTITY_NAME = "Project";
  public static final String TABLE_NAME = "project";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_LONGDESCRHTML_NAME = "long_descr_html";
  public static final String COLUMN_GENERATOR_NAME = "generator";
  public static final String COLUMN_FAVORITE_NAME = "favorite";
  public static final String COLUMN_THUMBSSTATE_NAME = "thumbs_state";
  public static final String COLUMN_THUMBSSTATEREASON_NAME = "thumbs_state_reason";
  public static final String COLUMN_ARCHIVED_NAME = "archived";
  public static final String COLUMN_ACTIVE_NAME = "active";
  private static final long serialVersionUID = 1849850730655062598L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String name;

  private String shortDescr;

  private String longDescrHtml;

  private String generator;

  private Boolean favorite;

  private String thumbsState;

  private String thumbsStateReason;

  private Boolean archived;

  private Boolean active;

  private Assignment assignment;

  private ProjectInput projectInput;

  private Set<LogReference> logReferences = new LinkedHashSet<>();

  private Set<ProjectImage> projectImages = new LinkedHashSet<>();

  private Set<ProjectInput> projectInputs = new LinkedHashSet<>();

  private Set<ProjectInputFulfillment> projectInputFulfillments = new LinkedHashSet<>();

  private Set<ProjectMilestone> projectMilestones = new LinkedHashSet<>();

  private Set<ProjectPost> projectPosts = new LinkedHashSet<>();

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
  @Column(name = COLUMN_SHORTDESCR_NAME)
  public String getShortDescr() {
    return shortDescr;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCRHTML_NAME)
  public String getLongDescrHtml() {
    return longDescrHtml;
  }

  @Lob
  @Column(name = COLUMN_GENERATOR_NAME)
  public String getGenerator() {
    return generator;
  }

  @Column(name = COLUMN_FAVORITE_NAME)
  public Boolean getFavorite() {
    return favorite;
  }

  @Lob
  @Column(name = COLUMN_THUMBSSTATE_NAME)
  public String getThumbsState() {
    return thumbsState;
  }

  @Lob
  @Column(name = COLUMN_THUMBSSTATEREASON_NAME)
  public String getThumbsStateReason() {
    return thumbsStateReason;
  }

  @Column(name = COLUMN_ARCHIVED_NAME)
  public Boolean getArchived() {
    return archived;
  }

  @Column(name = COLUMN_ACTIVE_NAME)
  public Boolean getActive() {
    return active;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assignment_id")
  public Assignment getAssignment() {
    return assignment;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_input_id")
  @PropagateDeleteFrom
  public ProjectInput getProjectInput() {
    return projectInput;
  }

  @OneToMany(mappedBy = "project")
  public Set<LogReference> getLogReferences() {
    return logReferences;
  }

  @OneToMany(mappedBy = "project")
  public Set<ProjectImage> getProjectImages() {
    return projectImages;
  }

  @OneToMany(mappedBy = "existingProject")
  public Set<ProjectInput> getProjectInputs() {
    return projectInputs;
  }

  @OneToMany(mappedBy = "project")
  public Set<ProjectInputFulfillment> getProjectInputFulfillments() {
    return projectInputFulfillments;
  }

  @OneToMany(mappedBy = "project")
  public Set<ProjectMilestone> getProjectMilestones() {
    return projectMilestones;
  }

  @OneToMany(mappedBy = "project")
  public Set<ProjectPost> getProjectPosts() {
    return projectPosts;
  }

  @OneToMany(mappedBy = "project")
  public Set<Tag> getTags() {
    return tags;
  }
}
