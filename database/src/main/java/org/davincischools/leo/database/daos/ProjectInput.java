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

@Entity(name = ProjectInput.ENTITY_NAME)
@Table(name = ProjectInput.TABLE_NAME, schema = "leo_temp")
public class ProjectInput implements Serializable {

  public static final String ENTITY_NAME = "ProjectInput";
  public static final String TABLE_NAME = "project_input";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_TIMEOUT_NAME = "timeout";
  public static final String COLUMN_STATE_NAME = "state";
  private static final long serialVersionUID = -7093946939269551820L;

  private Integer id;

  private Instant creationTime;

  private Instant timeout;

  private String state;

  private ProjectDefinition projectDefinition;

  private Student student;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public ProjectInput setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public ProjectInput setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_TIMEOUT_NAME)
  public Instant getTimeout() {
    return timeout;
  }

  public ProjectInput setTimeout(Instant timeout) {
    this.timeout = timeout;
    return this;
  }

  @Column(name = COLUMN_STATE_NAME, nullable = false, length = 10)
  public String getState() {
    return state;
  }

  public ProjectInput setState(String state) {
    this.state = state;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_definition_id")
  public ProjectDefinition getProjectDefinition() {
    return projectDefinition;
  }

  public ProjectInput setProjectDefinition(ProjectDefinition projectDefinition) {
    this.projectDefinition = projectDefinition;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "student_id", nullable = false)
  public Student getStudent() {
    return student;
  }

  public ProjectInput setStudent(Student student) {
    this.student = student;
    return this;
  }
}
