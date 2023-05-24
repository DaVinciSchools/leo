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

@Entity(name = ProjectCycleStep.ENTITY_NAME)
@Table(name = ProjectCycleStep.TABLE_NAME, schema = "leo_temp")
public class ProjectCycleStep implements Serializable {

  public static final String ENTITY_NAME = "ProjectCycleStep";
  public static final String TABLE_NAME = "project_cycle_step";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_POSITION_NAME = "position";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_SHORTDESCRQUILL_NAME = "short_descr_quill";
  public static final String COLUMN_LONGDESCR_NAME = "long_descr";
  public static final String COLUMN_LONGDESCRQUILL_NAME = "long_descr_quill";
  private static final long serialVersionUID = -1897891964958123871L;

  private Integer id;

  private Instant creationTime;

  private Integer position;

  private String name;

  private String shortDescr;

  private String shortDescrQuill;

  private String longDescr;

  private String longDescrQuill;

  private ProjectCycle projectCycle;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public ProjectCycleStep setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public ProjectCycleStep setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_POSITION_NAME, nullable = false)
  public Integer getPosition() {
    return position;
  }

  public ProjectCycleStep setPosition(Integer position) {
    this.position = position;
    return this;
  }

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public ProjectCycleStep setName(String name) {
    this.name = name;
    return this;
  }

  @Lob
  @Column(name = COLUMN_SHORTDESCR_NAME)
  public String getShortDescr() {
    return shortDescr;
  }

  public ProjectCycleStep setShortDescr(String shortDescr) {
    this.shortDescr = shortDescr;
    return this;
  }

  @Lob
  @Column(name = COLUMN_SHORTDESCRQUILL_NAME)
  public String getShortDescrQuill() {
    return shortDescrQuill;
  }

  public ProjectCycleStep setShortDescrQuill(String shortDescrQuill) {
    this.shortDescrQuill = shortDescrQuill;
    return this;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCR_NAME)
  public String getLongDescr() {
    return longDescr;
  }

  public ProjectCycleStep setLongDescr(String longDescr) {
    this.longDescr = longDescr;
    return this;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCRQUILL_NAME)
  public String getLongDescrQuill() {
    return longDescrQuill;
  }

  public ProjectCycleStep setLongDescrQuill(String longDescrQuill) {
    this.longDescrQuill = longDescrQuill;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_cycle_id", nullable = false)
  public ProjectCycle getProjectCycle() {
    return projectCycle;
  }

  public ProjectCycleStep setProjectCycle(ProjectCycle projectCycle) {
    this.projectCycle = projectCycle;
    return this;
  }
}
