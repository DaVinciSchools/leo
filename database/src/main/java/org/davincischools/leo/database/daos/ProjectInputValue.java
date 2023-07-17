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
    indexes = {
      @Index(name = "project_input_category_id", columnList = "project_input_category_id")
    })
public class ProjectInputValue implements Serializable {

  public static final String ENTITY_NAME = "ProjectInputValue";
  public static final String TABLE_NAME = "project_input_value";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_POSITION_NAME = "position";
  public static final String COLUMN_FREETEXTVALUE_NAME = "free_text_value";
  private static final long serialVersionUID = 2138494411047959251L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  @Deprecated private Float position;

  private ProjectInput projectInput;

  @Deprecated private ProjectInputCategory projectInputCategory;

  private ProjectDefinitionCategory projectDefinitionCategory;

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

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  public ProjectInputValue setDeleted(Instant deleted) {
    this.deleted = deleted;
    return this;
  }

  @Deprecated
  @Column(name = COLUMN_POSITION_NAME, nullable = false)
  public Float getPosition() {
    return position;
  }

  @Deprecated
  public ProjectInputValue setPosition(Float position) {
    this.position = position;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_input_id", nullable = false)
  public ProjectInput getProjectInput() {
    return projectInput;
  }

  public ProjectInputValue setProjectInput(ProjectInput projectInput) {
    this.projectInput = projectInput;
    return this;
  }

  @Deprecated
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_input_category_id")
  public ProjectInputCategory getProjectInputCategory() {
    return projectInputCategory;
  }

  @Deprecated
  public ProjectInputValue setProjectInputCategory(ProjectInputCategory projectInputCategory) {
    this.projectInputCategory = projectInputCategory;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_definition_category_id")
  public ProjectDefinitionCategory getProjectDefinitionCategory() {
    return projectDefinitionCategory;
  }

  public ProjectInputValue setProjectDefinitionCategory(
      ProjectDefinitionCategory projectDefinitionCategory) {
    this.projectDefinitionCategory = projectDefinitionCategory;
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
