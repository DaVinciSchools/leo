package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.davincischools.leo.database.dao_interfaces.DaoWithPosition;
import org.davincischools.leo.database.dao_interfaces.PropagateDeleteFrom;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity(name = ProjectDefinitionCategory.ENTITY_NAME)
@Table(name = ProjectDefinitionCategory.TABLE_NAME, schema = "leo_test")
public class ProjectDefinitionCategory implements Serializable, DaoWithPosition {

  public static final String ENTITY_NAME = "ProjectDefinitionCategory";
  public static final String TABLE_NAME = "project_definition_category";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_POSITION_NAME = "position";
  public static final String COLUMN_MAXNUMVALUES_NAME = "max_num_values";
  @Serial private static final long serialVersionUID = 8710240537712217289L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private Float position;

  private Integer maxNumValues;

  private ProjectDefinitionCategoryType projectDefinitionCategoryType;

  private ProjectDefinition projectDefinition;

  private Set<ProjectInputValue> projectInputValues = new LinkedHashSet<>();

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

  @Override
  @Column(name = COLUMN_POSITION_NAME, nullable = false)
  public Float getPosition() {
    return position;
  }

  @Column(name = COLUMN_MAXNUMVALUES_NAME, nullable = false)
  public Integer getMaxNumValues() {
    return maxNumValues;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_definition_category_type_id", nullable = false)
  @PropagateDeleteFrom
  public ProjectDefinitionCategoryType getProjectDefinitionCategoryType() {
    return projectDefinitionCategoryType;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_definition_id", nullable = false)
  @PropagateDeleteFrom
  public ProjectDefinition getProjectDefinition() {
    return projectDefinition;
  }

  @OneToMany(mappedBy = "projectDefinitionCategory")
  public Set<ProjectInputValue> getProjectInputValues() {
    return projectInputValues;
  }
}
