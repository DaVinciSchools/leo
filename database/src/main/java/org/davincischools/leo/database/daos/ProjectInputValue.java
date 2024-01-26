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
import lombok.experimental.Accessors;
import org.davincischools.leo.database.dao_interfaces.DaoWithPosition;
import org.davincischools.leo.database.dao_interfaces.PropagateDeleteFrom;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity(name = ProjectInputValue.ENTITY_NAME)
@Table(name = ProjectInputValue.TABLE_NAME, schema = "leo_test")
public class ProjectInputValue implements Serializable, DaoWithPosition {

  public static final String ENTITY_NAME = "ProjectInputValue";
  public static final String TABLE_NAME = "project_input_value";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_POSITION_NAME = "position";
  public static final String COLUMN_FREETEXTVALUE_NAME = "free_text_value";
  private static final long serialVersionUID = -3819840364689001430L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private Float position;

  private ProjectInput projectInput;

  private ProjectDefinitionCategory projectDefinitionCategory;

  private String freeTextValue;

  private KnowledgeAndSkill knowledgeAndSkillValue;

  private Motivation motivationValue;

  private Set<ProjectInputFulfillment> projectInputFulfillments = new LinkedHashSet<>();

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

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_input_id", nullable = false)
  @PropagateDeleteFrom
  public ProjectInput getProjectInput() {
    return projectInput;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_definition_category_id", nullable = false)
  @PropagateDeleteFrom
  public ProjectDefinitionCategory getProjectDefinitionCategory() {
    return projectDefinitionCategory;
  }

  @Lob
  @Column(name = COLUMN_FREETEXTVALUE_NAME)
  public String getFreeTextValue() {
    return freeTextValue;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "knowledge_and_skill_value_id")
  public KnowledgeAndSkill getKnowledgeAndSkillValue() {
    return knowledgeAndSkillValue;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "motivation_value_id")
  public Motivation getMotivationValue() {
    return motivationValue;
  }

  @OneToMany(mappedBy = "projectInputValue")
  public Set<ProjectInputFulfillment> getProjectInputFulfillments() {
    return projectInputFulfillments;
  }
}
