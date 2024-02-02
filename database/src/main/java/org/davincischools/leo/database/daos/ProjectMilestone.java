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
import org.davincischools.leo.database.dao_interfaces.DaoWithPosition;
import org.davincischools.leo.database.dao_interfaces.PropagateDeleteFrom;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity(name = ProjectMilestone.ENTITY_NAME)
@Table(name = ProjectMilestone.TABLE_NAME, schema = "leo_test")
public class ProjectMilestone implements Serializable, DaoWithPosition {

  public static final String ENTITY_NAME = "ProjectMilestone";
  public static final String TABLE_NAME = "project_milestone";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_POSITION_NAME = "position";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_LONGDESCRHTML_NAME = "long_descr_html";
  @Serial private static final long serialVersionUID = 3299108172136654836L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private Float position;

  private String name;

  private String shortDescr;

  private String longDescrHtml;

  private Project project;

  private Set<ProjectMilestoneStep> projectMilestoneSteps = new LinkedHashSet<>();

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

  @Override
  @Column(name = COLUMN_POSITION_NAME, nullable = false)
  public Float getPosition() {
    return position;
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

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", nullable = false)
  @PropagateDeleteFrom
  public Project getProject() {
    return project;
  }

  @OneToMany(mappedBy = "projectMilestone")
  public Set<ProjectMilestoneStep> getProjectMilestoneSteps() {
    return projectMilestoneSteps;
  }
}
