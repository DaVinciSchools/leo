package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity(name = ProjectInputCategory.ENTITY_NAME)
@Table(name = ProjectInputCategory.TABLE_NAME, schema = "leo_temp")
public class ProjectInputCategory implements Serializable {

  public static final String ENTITY_NAME = "ProjectInputCategory";
  public static final String TABLE_NAME = "project_input_category";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_POSITION_NAME = "position";
  public static final String COLUMN_TITLE_NAME = "title";
  public static final String COLUMN_HINT_NAME = "hint";
  public static final String COLUMN_INPUTDESCR_NAME = "input_descr";
  public static final String COLUMN_INPUTPLACEHOLDER_NAME = "input_placeholder";
  public static final String COLUMN_QUERYPREFIX_NAME = "query_prefix";
  public static final String COLUMN_VALUETYPE_NAME = "value_type";
  public static final String COLUMN_MAXNUMVALUES_NAME = "max_num_values";
  private static final long serialVersionUID = -5764083503818819240L;

  private Integer id;

  private Instant creationTime;

  private String shortDescr;

  private Integer position;

  private String title;

  private String hint;

  private String inputDescr;

  private String inputPlaceholder;

  private String queryPrefix;

  private String valueType;

  private Integer maxNumValues;

  private ProjectDefinition projectDefinition;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public ProjectInputCategory setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public ProjectInputCategory setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_SHORTDESCR_NAME, nullable = false)
  public String getShortDescr() {
    return shortDescr;
  }

  public ProjectInputCategory setShortDescr(String shortDescr) {
    this.shortDescr = shortDescr;
    return this;
  }

  @Column(name = COLUMN_POSITION_NAME, nullable = false)
  public Integer getPosition() {
    return position;
  }

  public ProjectInputCategory setPosition(Integer position) {
    this.position = position;
    return this;
  }

  @Column(name = COLUMN_TITLE_NAME, nullable = false)
  public String getTitle() {
    return title;
  }

  public ProjectInputCategory setTitle(String title) {
    this.title = title;
    return this;
  }

  @Column(name = COLUMN_HINT_NAME, nullable = false)
  public String getHint() {
    return hint;
  }

  public ProjectInputCategory setHint(String hint) {
    this.hint = hint;
    return this;
  }

  @Column(name = COLUMN_INPUTDESCR_NAME, nullable = false)
  public String getInputDescr() {
    return inputDescr;
  }

  public ProjectInputCategory setInputDescr(String inputDescr) {
    this.inputDescr = inputDescr;
    return this;
  }

  @Column(name = COLUMN_INPUTPLACEHOLDER_NAME, nullable = false)
  public String getInputPlaceholder() {
    return inputPlaceholder;
  }

  public ProjectInputCategory setInputPlaceholder(String inputPlaceholder) {
    this.inputPlaceholder = inputPlaceholder;
    return this;
  }

  @Column(name = COLUMN_QUERYPREFIX_NAME, nullable = false)
  public String getQueryPrefix() {
    return queryPrefix;
  }

  public ProjectInputCategory setQueryPrefix(String queryPrefix) {
    this.queryPrefix = queryPrefix;
    return this;
  }

  @Lob
  @Column(name = COLUMN_VALUETYPE_NAME, nullable = false)
  public String getValueType() {
    return valueType;
  }

  public ProjectInputCategory setValueType(String valueType) {
    this.valueType = valueType;
    return this;
  }

  @Column(name = COLUMN_MAXNUMVALUES_NAME, nullable = false)
  public Integer getMaxNumValues() {
    return maxNumValues;
  }

  public ProjectInputCategory setMaxNumValues(Integer maxNumValues) {
    this.maxNumValues = maxNumValues;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_definition_id", nullable = false)
  public ProjectDefinition getProjectDefinition() {
    return projectDefinition;
  }

  public ProjectInputCategory setProjectDefinition(ProjectDefinition projectDefinition) {
    this.projectDefinition = projectDefinition;
    return this;
  }
}
