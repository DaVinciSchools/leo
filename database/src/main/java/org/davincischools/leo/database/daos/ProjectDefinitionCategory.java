package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity(name = ProjectDefinitionCategory.ENTITY_NAME)
@Table(name = ProjectDefinitionCategory.TABLE_NAME, schema = "leo_temp")
public class ProjectDefinitionCategory implements Serializable {

  public static final String ENTITY_NAME = "ProjectDefinitionCategory";
  public static final String TABLE_NAME = "project_definition_category";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_POSITION_NAME = "position";
  public static final String COLUMN_MAXNUMVALUES_NAME = "max_num_values";
  private static final long serialVersionUID = -3957907474676954712L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private Float position;

  private Integer maxNumValues;

  private ProjectDefinitionCategoryType projectDefinitionCategoryType;

  private ProjectDefinition projectDefinition;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public ProjectDefinitionCategory setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public ProjectDefinitionCategory setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  public ProjectDefinitionCategory setDeleted(Instant deleted) {
    this.deleted = deleted;
    return this;
  }

  @Column(name = COLUMN_POSITION_NAME, nullable = false)
  public Float getPosition() {
    return position;
  }

  public ProjectDefinitionCategory setPosition(Float position) {
    this.position = position;
    return this;
  }

  @Column(name = COLUMN_MAXNUMVALUES_NAME, nullable = false)
  public Integer getMaxNumValues() {
    return maxNumValues;
  }

  public ProjectDefinitionCategory setMaxNumValues(Integer maxNumValues) {
    this.maxNumValues = maxNumValues;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_definition_category_type_id", nullable = false)
  public ProjectDefinitionCategoryType getProjectDefinitionCategoryType() {
    return projectDefinitionCategoryType;
  }

  public ProjectDefinitionCategory setProjectDefinitionCategoryType(
      ProjectDefinitionCategoryType projectDefinitionCategoryType) {
    this.projectDefinitionCategoryType = projectDefinitionCategoryType;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_definition_id", nullable = false)
  public ProjectDefinition getProjectDefinition() {
    return projectDefinition;
  }

  public ProjectDefinitionCategory setProjectDefinition(ProjectDefinition projectDefinition) {
    this.projectDefinition = projectDefinition;
    return this;
  }
}
