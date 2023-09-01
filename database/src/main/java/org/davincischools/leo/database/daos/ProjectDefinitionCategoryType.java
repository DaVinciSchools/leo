package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
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
@Entity(name = ProjectDefinitionCategoryType.ENTITY_NAME)
@Table(name = ProjectDefinitionCategoryType.TABLE_NAME, schema = "leo_temp")
public class ProjectDefinitionCategoryType implements Serializable {

  public static final String ENTITY_NAME = "ProjectDefinitionCategoryType";
  public static final String TABLE_NAME = "project_definition_category_type";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_INCLUDEINDEMO_NAME = "include_in_demo";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_HINT_NAME = "hint";
  public static final String COLUMN_INPUTDESCR_NAME = "input_descr";
  public static final String COLUMN_INPUTPLACEHOLDER_NAME = "input_placeholder";
  public static final String COLUMN_QUERYPREFIX_NAME = "query_prefix";
  public static final String COLUMN_VALUETYPE_NAME = "value_type";
  private static final long serialVersionUID = -3808291522036685427L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String shortDescr;

  private Boolean includeInDemo;

  private String name;

  private String hint;

  private String inputDescr;

  private String inputPlaceholder;

  private String queryPrefix;

  private String valueType;

  private Set<ProjectDefinitionCategory> projectDefinitionCategories = new LinkedHashSet<>();

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  @Column(name = COLUMN_SHORTDESCR_NAME, nullable = false, length = 1024)
  public String getShortDescr() {
    return shortDescr;
  }

  @Column(name = COLUMN_INCLUDEINDEMO_NAME)
  public Boolean getIncludeInDemo() {
    return includeInDemo;
  }

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  @Column(name = COLUMN_HINT_NAME, nullable = false)
  public String getHint() {
    return hint;
  }

  @Column(name = COLUMN_INPUTDESCR_NAME, nullable = false, length = 1024)
  public String getInputDescr() {
    return inputDescr;
  }

  @Column(name = COLUMN_INPUTPLACEHOLDER_NAME, nullable = false)
  public String getInputPlaceholder() {
    return inputPlaceholder;
  }

  @Column(name = COLUMN_QUERYPREFIX_NAME, nullable = false)
  public String getQueryPrefix() {
    return queryPrefix;
  }

  @Lob
  @Column(name = COLUMN_VALUETYPE_NAME, nullable = false)
  public String getValueType() {
    return valueType;
  }

  @OneToMany(mappedBy = "projectDefinitionCategoryType")
  public Set<ProjectDefinitionCategory> getProjectDefinitionCategories() {
    return projectDefinitionCategories;
  }
}
