package org.davincischools.leo.database.daos;

import static org.davincischools.leo.database.utils.DaoUtils.getDaoClass;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
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
@Embeddable
public class ClassXKnowledgeAndSkillId implements Serializable {

  public static final String COLUMN_CLASSXID_NAME = "class_x_id";
  public static final String COLUMN_KNOWLEDGEANDSKILLID_NAME = "knowledge_and_skill_id";
  private static final long serialVersionUID = -2204584561480220248L;

  private Integer classXId;

  private Integer knowledgeAndSkillId;

  @Column(name = COLUMN_CLASSXID_NAME, nullable = false)
  public Integer getClassXId() {
    return classXId;
  }

  @Column(name = COLUMN_KNOWLEDGEANDSKILLID_NAME, nullable = false)
  public Integer getKnowledgeAndSkillId() {
    return knowledgeAndSkillId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getDaoClass(this) != getDaoClass(o)) return false;
    ClassXKnowledgeAndSkillId entity = (ClassXKnowledgeAndSkillId) o;
    return Objects.equals(this.knowledgeAndSkillId, entity.knowledgeAndSkillId)
        && Objects.equals(this.classXId, entity.classXId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(knowledgeAndSkillId, classXId);
  }
}
