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

@Entity(name = ClassXKnowledgeAndSkill.ENTITY_NAME)
@Table(name = ClassXKnowledgeAndSkill.TABLE_NAME, schema = "leo_temp")
public class ClassXKnowledgeAndSkill implements Serializable {

  public static final String ENTITY_NAME = "ClassXKnowledgeAndSkill";
  public static final String TABLE_NAME = "class_x__knowledge_and_skill";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  private static final long serialVersionUID = 2115481320097609757L;

  private ClassXKnowledgeAndSkillId id;

  private ClassX classX;

  private KnowledgeAndSkill knowledgeAndSkill;

  private Instant creationTime;

  @EmbeddedId
  public ClassXKnowledgeAndSkillId getId() {
    return id;
  }

  public ClassXKnowledgeAndSkill setId(ClassXKnowledgeAndSkillId id) {
    this.id = id;
    return this;
  }

  @MapsId("classXId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "class_x_id", nullable = false)
  public ClassX getClassX() {
    return classX;
  }

  public ClassXKnowledgeAndSkill setClassX(ClassX classX) {
    this.classX = classX;
    return this;
  }

  @MapsId("knowledgeAndSkillId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "knowledge_and_skill_id", nullable = false)
  public KnowledgeAndSkill getKnowledgeAndSkill() {
    return knowledgeAndSkill;
  }

  public ClassXKnowledgeAndSkill setKnowledgeAndSkill(KnowledgeAndSkill knowledgeAndSkill) {
    this.knowledgeAndSkill = knowledgeAndSkill;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public ClassXKnowledgeAndSkill setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }
}
