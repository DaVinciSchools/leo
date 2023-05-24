package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity(name = ProjectInputValue.ENTITY_NAME)
@Table(
    name = ProjectInputValue.TABLE_NAME,
    schema = "leo_temp",
    indexes = {@Index(name = "input_category_id", columnList = "input_category_id")})
public class ProjectInputValue implements Serializable {

  public static final String ENTITY_NAME = "ProjectInputValue";
  public static final String TABLE_NAME = "project_input_value";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_POSITION_NAME = "position";
  public static final String COLUMN_FREETEXTVALUE_NAME = "free_text_value";
  private static final long serialVersionUID = -1012220161446923846L;

  private Integer id;

  private Instant creationTime;

  private ProjectInputCategory inputCategory;

  private Integer position;

  private String freeTextValue;

  private KnowledgeAndSkill knowledgeAndSkillValue;

  private Motivation motivationValue;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public ProjectInputValue setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public ProjectInputValue setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "input_category_id", nullable = false)
  public ProjectInputCategory getInputCategory() {
    return inputCategory;
  }

  public ProjectInputValue setInputCategory(ProjectInputCategory inputCategory) {
    this.inputCategory = inputCategory;
    return this;
  }

  @Column(name = COLUMN_POSITION_NAME, nullable = false)
  public Integer getPosition() {
    return position;
  }

  public ProjectInputValue setPosition(Integer position) {
    this.position = position;
    return this;
  }

  @Lob
  @Column(name = COLUMN_FREETEXTVALUE_NAME)
  public String getFreeTextValue() {
    return freeTextValue;
  }

  public ProjectInputValue setFreeTextValue(String freeTextValue) {
    this.freeTextValue = freeTextValue;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "knowledge_and_skill_value_id")
  public KnowledgeAndSkill getKnowledgeAndSkillValue() {
    return knowledgeAndSkillValue;
  }

  public ProjectInputValue setKnowledgeAndSkillValue(KnowledgeAndSkill knowledgeAndSkillValue) {
    this.knowledgeAndSkillValue = knowledgeAndSkillValue;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "motivation_value_id")
  public Motivation getMotivationValue() {
    return motivationValue;
  }

  public ProjectInputValue setMotivationValue(Motivation motivationValue) {
    this.motivationValue = motivationValue;
    return this;
  }
}
