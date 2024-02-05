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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity(name = ProjectDefinition.ENTITY_NAME)
@Table(name = ProjectDefinition.TABLE_NAME, schema = "leo_test")
public class ProjectDefinition implements Serializable {

  public static final String ENTITY_NAME = "ProjectDefinition";
  public static final String TABLE_NAME = "project_definition";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_TEMPLATE_NAME = "template";
  @Serial private static final long serialVersionUID = 4310536244888704608L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String name;

  private Boolean template;

  private UserX userX;

  private Set<AssignmentProjectDefinition> assignmentProjectDefinitions = new LinkedHashSet<>();

  private Set<ProjectDefinitionCategory> projectDefinitionCategories = new LinkedHashSet<>();

  private Set<ProjectInput> projectInputs = new LinkedHashSet<>();

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

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  @Column(name = COLUMN_TEMPLATE_NAME)
  public Boolean getTemplate() {
    return template;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_x_id")
  public UserX getUserX() {
    return userX;
  }

  @OneToMany(mappedBy = "projectDefinition")
  public Set<AssignmentProjectDefinition> getAssignmentProjectDefinitions() {
    return assignmentProjectDefinitions;
  }

  @OneToMany(mappedBy = "projectDefinition")
  public Set<ProjectDefinitionCategory> getProjectDefinitionCategories() {
    return projectDefinitionCategories;
  }

  @OneToMany(mappedBy = "projectDefinition")
  public Set<ProjectInput> getProjectInputs() {
    return projectInputs;
  }
}
