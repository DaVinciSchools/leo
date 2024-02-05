package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
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
@Entity(name = ProjectInputAssignment.ENTITY_NAME)
@Table(name = ProjectInputAssignment.TABLE_NAME, schema = "leo_test")
public class ProjectInputAssignment implements Serializable {

  public static final String ENTITY_NAME = "ProjectInputAssignment";
  public static final String TABLE_NAME = "project_input__assignment";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  @Serial private static final long serialVersionUID = 1265768437301421271L;

  private ProjectInputAssignmentId id;

  private ProjectInput projectInput;

  private Assignment assignment;

  private Instant creationTime;

  private Instant deleted;

  @EmbeddedId
  public ProjectInputAssignmentId getId() {
    return id;
  }

  @MapsId("projectInputId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_input_id", nullable = false)
  @PropagateDeleteFrom
  public ProjectInput getProjectInput() {
    return projectInput;
  }

  @MapsId("assignmentId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "assignment_id", nullable = false)
  @PropagateDeleteFrom
  public Assignment getAssignment() {
    return assignment;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }
}
