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
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Embeddable
public class AssignmentProjectDefinitionId implements Serializable {

  public static final String COLUMN_ASSIGNMENTID_NAME = "assignment_id";
  public static final String COLUMN_PROJECTDEFINITIONID_NAME = "project_definition_id";
  private static final long serialVersionUID = -4000282302441721923L;

  private Integer assignmentId;

  private Integer projectDefinitionId;

  @Column(name = COLUMN_ASSIGNMENTID_NAME, nullable = false)
  public Integer getAssignmentId() {
    return assignmentId;
  }

  @Column(name = COLUMN_PROJECTDEFINITIONID_NAME, nullable = false)
  public Integer getProjectDefinitionId() {
    return projectDefinitionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getDaoClass(this) != getDaoClass(o)) return false;
    AssignmentProjectDefinitionId entity = (AssignmentProjectDefinitionId) o;
    return Objects.equals(this.projectDefinitionId, entity.projectDefinitionId)
        && Objects.equals(this.assignmentId, entity.assignmentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(projectDefinitionId, assignmentId);
  }
}
