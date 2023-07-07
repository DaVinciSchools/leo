package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity(name = AssignmentProjectDefinition.ENTITY_NAME)
@Table(name = AssignmentProjectDefinition.TABLE_NAME, schema = "leo_temp")
public class AssignmentProjectDefinition implements Serializable {

  public static final String ENTITY_NAME = "AssignmentProjectDefinition";
  public static final String TABLE_NAME = "assignment__project_definition";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_SELECTED_NAME = "selected";
  private static final long serialVersionUID = 7898261928448484961L;

  private AssignmentProjectDefinitionId id;

  private Assignment assignment;

  private ProjectDefinition projectDefinition;

  private Instant creationTime;

  private Instant deleted;

  private Boolean selected;

  @EmbeddedId
  public AssignmentProjectDefinitionId getId() {
    return id;
  }

  public AssignmentProjectDefinition setId(AssignmentProjectDefinitionId id) {
    this.id = id;
    return this;
  }

  @MapsId("assignmentId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "assignment_id", nullable = false)
  public Assignment getAssignment() {
    return assignment;
  }

  public AssignmentProjectDefinition setAssignment(Assignment assignment) {
    this.assignment = assignment;
    return this;
  }

  @MapsId("projectDefinitionId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_definition_id", nullable = false)
  public ProjectDefinition getProjectDefinition() {
    return projectDefinition;
  }

  public AssignmentProjectDefinition setProjectDefinition(ProjectDefinition projectDefinition) {
    this.projectDefinition = projectDefinition;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public AssignmentProjectDefinition setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  public AssignmentProjectDefinition setDeleted(Instant deleted) {
    this.deleted = deleted;
    return this;
  }

  @Column(name = COLUMN_SELECTED_NAME)
  public Boolean getSelected() {
    return selected;
  }

  public AssignmentProjectDefinition setSelected(Boolean selected) {
    this.selected = selected;
    return this;
  }
}
