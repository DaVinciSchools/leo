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

@Entity(name = ProjectMilestoneStep.ENTITY_NAME)
@Table(name = ProjectMilestoneStep.TABLE_NAME, schema = "leo_temp")
public class ProjectMilestoneStep implements Serializable {

  public static final String ENTITY_NAME = "ProjectMilestoneStep";
  public static final String TABLE_NAME = "project_milestone_step";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_POSITION_NAME = "position";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_SHORTDESCRQUILL_NAME = "short_descr_quill";
  public static final String COLUMN_LONGDESCR_NAME = "long_descr";
  public static final String COLUMN_LONGDESCRQUILL_NAME = "long_descr_quill";
  private static final long serialVersionUID = 6649396949785151885L;

  private Integer id;

  private Instant creationTime;

  private Integer position;

  private String name;

  private String shortDescr;

  private String shortDescrQuill;

  private String longDescr;

  private String longDescrQuill;

  private ProjectMilestone projectMilestone;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public ProjectMilestoneStep setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public ProjectMilestoneStep setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_POSITION_NAME, nullable = false)
  public Integer getPosition() {
    return position;
  }

  public ProjectMilestoneStep setPosition(Integer position) {
    this.position = position;
    return this;
  }

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public ProjectMilestoneStep setName(String name) {
    this.name = name;
    return this;
  }

  @Lob
  @Column(name = COLUMN_SHORTDESCR_NAME)
  public String getShortDescr() {
    return shortDescr;
  }

  public ProjectMilestoneStep setShortDescr(String shortDescr) {
    this.shortDescr = shortDescr;
    return this;
  }

  @Lob
  @Column(name = COLUMN_SHORTDESCRQUILL_NAME)
  public String getShortDescrQuill() {
    return shortDescrQuill;
  }

  public ProjectMilestoneStep setShortDescrQuill(String shortDescrQuill) {
    this.shortDescrQuill = shortDescrQuill;
    return this;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCR_NAME)
  public String getLongDescr() {
    return longDescr;
  }

  public ProjectMilestoneStep setLongDescr(String longDescr) {
    this.longDescr = longDescr;
    return this;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCRQUILL_NAME)
  public String getLongDescrQuill() {
    return longDescrQuill;
  }

  public ProjectMilestoneStep setLongDescrQuill(String longDescrQuill) {
    this.longDescrQuill = longDescrQuill;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_milestone_id", nullable = false)
  public ProjectMilestone getProjectMilestone() {
    return projectMilestone;
  }

  public ProjectMilestoneStep setProjectMilestone(ProjectMilestone projectMilestone) {
    this.projectMilestone = projectMilestone;
    return this;
  }
}
