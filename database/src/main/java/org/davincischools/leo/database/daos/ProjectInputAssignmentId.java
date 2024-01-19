package org.davincischools.leo.database.daos;

import static org.davincischools.leo.database.utils.DaoUtils.getDaoClass;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Embeddable
public class ProjectInputAssignmentId implements Serializable {

  public static final String COLUMN_PROJECTINPUTID_NAME = "project_input_id";
  public static final String COLUMN_ASSIGNMENTID_NAME = "assignment_id";
  private static final long serialVersionUID = 8725254907708097253L;

  private Integer projectInputId;

  private Integer assignmentId;

  @Column(name = COLUMN_PROJECTINPUTID_NAME, nullable = false)
  public Integer getProjectInputId() {
    return projectInputId;
  }

  @Column(name = COLUMN_ASSIGNMENTID_NAME, nullable = false)
  public Integer getAssignmentId() {
    return assignmentId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getDaoClass(this) != getDaoClass(o)) return false;
    ProjectInputAssignmentId entity = (ProjectInputAssignmentId) o;
    return Objects.equals(this.projectInputId, entity.projectInputId)
        && Objects.equals(this.assignmentId, entity.assignmentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(projectInputId, assignmentId);
  }
}
