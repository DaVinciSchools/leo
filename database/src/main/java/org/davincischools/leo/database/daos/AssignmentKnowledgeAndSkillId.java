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
public class AssignmentKnowledgeAndSkillId implements Serializable {

  public static final String COLUMN_ASSIGNMENTID_NAME = "assignment_id";
  public static final String COLUMN_KNOWLEDGEANDSKILLID_NAME = "knowledge_and_skill_id";
  private static final long serialVersionUID = 6745215174674869664L;

  private Integer assignmentId;

  private Integer knowledgeAndSkillId;

  @Column(name = COLUMN_ASSIGNMENTID_NAME, nullable = false)
  public Integer getAssignmentId() {
    return assignmentId;
  }

  @Column(name = COLUMN_KNOWLEDGEANDSKILLID_NAME, nullable = false)
  public Integer getKnowledgeAndSkillId() {
    return knowledgeAndSkillId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getDaoClass(this) != getDaoClass(o)) return false;
    AssignmentKnowledgeAndSkillId entity = (AssignmentKnowledgeAndSkillId) o;
    return Objects.equals(this.knowledgeAndSkillId, entity.knowledgeAndSkillId)
        && Objects.equals(this.assignmentId, entity.assignmentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(knowledgeAndSkillId, assignmentId);
  }
}
