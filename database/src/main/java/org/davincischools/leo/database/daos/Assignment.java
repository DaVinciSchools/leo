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
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity(name = Assignment.ENTITY_NAME)
@Table(name = Assignment.TABLE_NAME, schema = "leo_temp")
public class Assignment implements Serializable {

  public static final String ENTITY_NAME = "Assignment";
  public static final String TABLE_NAME = "assignment";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_NICKNAME_NAME = "nickname";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_LONGDESCRHTML_NAME = "long_descr_html";
  private static final long serialVersionUID = 5360829905417544151L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String name;

  private String nickname;

  private String shortDescr;

  private String longDescrHtml;

  private ClassX classX;

  private Set<AssignmentKnowledgeAndSkill> assignmentKnowledgeAndSkills = new LinkedHashSet<>();

  private Set<AssignmentProjectDefinition> assignmentProjectDefinitions = new LinkedHashSet<>();

  private Set<Project> projects = new LinkedHashSet<>();

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

  @Column(name = COLUMN_NICKNAME_NAME)
  public String getNickname() {
    return nickname;
  }

  @Lob
  @Column(name = COLUMN_SHORTDESCR_NAME)
  public String getShortDescr() {
    return shortDescr;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCRHTML_NAME)
  public String getLongDescrHtml() {
    return longDescrHtml;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "class_x_id")
  public ClassX getClassX() {
    return classX;
  }

  @OneToMany(mappedBy = "assignment")
  public Set<AssignmentKnowledgeAndSkill> getAssignmentKnowledgeAndSkills() {
    return assignmentKnowledgeAndSkills;
  }

  @OneToMany(mappedBy = "assignment")
  public Set<AssignmentProjectDefinition> getAssignmentProjectDefinitions() {
    return assignmentProjectDefinitions;
  }

  @OneToMany(mappedBy = "assignment")
  public Set<Project> getProjects() {
    return projects;
  }

  @OneToMany(mappedBy = "assignment")
  public Set<ProjectInput> getProjectInputs() {
    return projectInputs;
  }
}
