package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.Hibernate;

@Embeddable
public class ClassXKnowledgeAndSkillId implements Serializable {

  public static final String COLUMN_CLASSXID_NAME = "class_x_id";
  public static final String COLUMN_KNOWLEDGEANDSKILLID_NAME = "knowledge_and_skill_id";
  private static final long serialVersionUID = 9166491044106869364L;

  private Integer classXId;

  private Integer knowledgeAndSkillId;

  @Column(name = COLUMN_CLASSXID_NAME, nullable = false)
  public Integer getClassXId() {
    return classXId;
  }

  public ClassXKnowledgeAndSkillId setClassXId(Integer classXId) {
    this.classXId = classXId;
    return this;
  }

  @Column(name = COLUMN_KNOWLEDGEANDSKILLID_NAME, nullable = false)
  public Integer getKnowledgeAndSkillId() {
    return knowledgeAndSkillId;
  }

  public ClassXKnowledgeAndSkillId setKnowledgeAndSkillId(Integer knowledgeAndSkillId) {
    this.knowledgeAndSkillId = knowledgeAndSkillId;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    ClassXKnowledgeAndSkillId entity = (ClassXKnowledgeAndSkillId) o;
    return Objects.equals(this.knowledgeAndSkillId, entity.knowledgeAndSkillId)
        && Objects.equals(this.classXId, entity.classXId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(knowledgeAndSkillId, classXId);
  }
}
