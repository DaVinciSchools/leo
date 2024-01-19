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
public class ProjectAssignmentId implements Serializable {

  public static final String COLUMN_PROJECTID_NAME = "project_id";
  public static final String COLUMN_ASSIGNMENTID_NAME = "assignment_id";
  private static final long serialVersionUID = -6472836431686250753L;

  private Integer projectId;

  private Integer assignmentId;

  @Column(name = COLUMN_PROJECTID_NAME, nullable = false)
  public Integer getProjectId() {
    return projectId;
  }

  @Column(name = COLUMN_ASSIGNMENTID_NAME, nullable = false)
  public Integer getAssignmentId() {
    return assignmentId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getDaoClass(this) != getDaoClass(o)) return false;
    ProjectAssignmentId entity = (ProjectAssignmentId) o;
    return Objects.equals(this.projectId, entity.projectId)
        && Objects.equals(this.assignmentId, entity.assignmentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(projectId, assignmentId);
  }
}
