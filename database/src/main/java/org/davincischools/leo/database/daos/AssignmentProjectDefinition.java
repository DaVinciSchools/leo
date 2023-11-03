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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity(name = AssignmentProjectDefinition.ENTITY_NAME)
@Table(name = AssignmentProjectDefinition.TABLE_NAME, schema = "leo_test")
public class AssignmentProjectDefinition implements Serializable {

  public static final String ENTITY_NAME = "AssignmentProjectDefinition";
  public static final String TABLE_NAME = "assignment__project_definition";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_SELECTED_NAME = "selected";
  private static final long serialVersionUID = -4706522112826940998L;

  private AssignmentProjectDefinitionId id;

  private Assignment assignment;

  private ProjectDefinition projectDefinition;

  private Instant creationTime;

  private Instant deleted;

  private Instant selected;

  @EmbeddedId
  public AssignmentProjectDefinitionId getId() {
    return id;
  }

  @MapsId("assignmentId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "assignment_id", nullable = false)
  public Assignment getAssignment() {
    return assignment;
  }

  @MapsId("projectDefinitionId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_definition_id", nullable = false)
  public ProjectDefinition getProjectDefinition() {
    return projectDefinition;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  @Column(name = COLUMN_SELECTED_NAME)
  public Instant getSelected() {
    return selected;
  }
}
