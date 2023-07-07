package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity(name = LogReference.ENTITY_NAME)
@Table(name = LogReference.TABLE_NAME, schema = "leo_temp")
public class LogReference implements Serializable {

  public static final String ENTITY_NAME = "LogReference";
  public static final String TABLE_NAME = "log_reference";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  private static final long serialVersionUID = -4550416910066224224L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private Log log;

  private ProjectInput projectInput;

  private Project project;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public LogReference setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public LogReference setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  public LogReference setDeleted(Instant deleted) {
    this.deleted = deleted;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "log_id", nullable = false)
  public Log getLog() {
    return log;
  }

  public LogReference setLog(Log log) {
    this.log = log;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_input_id")
  public ProjectInput getProjectInput() {
    return projectInput;
  }

  public LogReference setProjectInput(ProjectInput projectInput) {
    this.projectInput = projectInput;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  public Project getProject() {
    return project;
  }

  public LogReference setProject(Project project) {
    this.project = project;
    return this;
  }
}
