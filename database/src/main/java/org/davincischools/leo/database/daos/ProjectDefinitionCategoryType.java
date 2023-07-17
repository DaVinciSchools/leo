package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity(name = ProjectDefinitionCategoryType.ENTITY_NAME)
@Table(name = ProjectDefinitionCategoryType.TABLE_NAME, schema = "leo_temp")
public class ProjectDefinitionCategoryType implements Serializable {

  public static final String ENTITY_NAME = "ProjectDefinitionCategoryType";
  public static final String TABLE_NAME = "project_definition_category_type";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_HINT_NAME = "hint";
  public static final String COLUMN_INPUTDESCR_NAME = "input_descr";
  public static final String COLUMN_INPUTPLACEHOLDER_NAME = "input_placeholder";
  public static final String COLUMN_QUERYPREFIX_NAME = "query_prefix";
  public static final String COLUMN_VALUETYPE_NAME = "value_type";
  private static final long serialVersionUID = -7633310181316823729L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String shortDescr;

  private String name;

  private String hint;

  private String inputDescr;

  private String inputPlaceholder;

  private String queryPrefix;

  private String valueType;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public ProjectDefinitionCategoryType setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public ProjectDefinitionCategoryType setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  public ProjectDefinitionCategoryType setDeleted(Instant deleted) {
    this.deleted = deleted;
    return this;
  }

  @Column(name = COLUMN_SHORTDESCR_NAME, nullable = false)
  public String getShortDescr() {
    return shortDescr;
  }

  public ProjectDefinitionCategoryType setShortDescr(String shortDescr) {
    this.shortDescr = shortDescr;
    return this;
  }

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public ProjectDefinitionCategoryType setName(String name) {
    this.name = name;
    return this;
  }

  @Column(name = COLUMN_HINT_NAME, nullable = false)
  public String getHint() {
    return hint;
  }

  public ProjectDefinitionCategoryType setHint(String hint) {
    this.hint = hint;
    return this;
  }

  @Column(name = COLUMN_INPUTDESCR_NAME, nullable = false)
  public String getInputDescr() {
    return inputDescr;
  }

  public ProjectDefinitionCategoryType setInputDescr(String inputDescr) {
    this.inputDescr = inputDescr;
    return this;
  }

  @Column(name = COLUMN_INPUTPLACEHOLDER_NAME, nullable = false)
  public String getInputPlaceholder() {
    return inputPlaceholder;
  }

  public ProjectDefinitionCategoryType setInputPlaceholder(String inputPlaceholder) {
    this.inputPlaceholder = inputPlaceholder;
    return this;
  }

  @Column(name = COLUMN_QUERYPREFIX_NAME, nullable = false)
  public String getQueryPrefix() {
    return queryPrefix;
  }

  public ProjectDefinitionCategoryType setQueryPrefix(String queryPrefix) {
    this.queryPrefix = queryPrefix;
    return this;
  }

  @Lob
  @Column(name = COLUMN_VALUETYPE_NAME, nullable = false)
  public String getValueType() {
    return valueType;
  }

  public ProjectDefinitionCategoryType setValueType(String valueType) {
    this.valueType = valueType;
    return this;
  }
}
