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
@Entity(name = KnowledgeAndSkill.ENTITY_NAME)
@Table(name = KnowledgeAndSkill.TABLE_NAME, schema = "leo_temp")
public class KnowledgeAndSkill implements Serializable {

  public static final String ENTITY_NAME = "KnowledgeAndSkill";
  public static final String TABLE_NAME = "knowledge_and_skill";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_TYPE_NAME = "type";
  public static final String COLUMN_CATEGORY_NAME = "category";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_LONGDESCRHTML_NAME = "long_descr_html";
  public static final String COLUMN_GLOBAL_NAME = "global";
  private static final long serialVersionUID = 2792480636886308959L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String name;

  private String type;

  private String category;

  private String shortDescr;

  private String longDescrHtml;

  private Boolean global;

  private UserX userX;

  private Set<AssignmentKnowledgeAndSkill> assignmentKnowledgeAndSkills = new LinkedHashSet<>();

  private Set<ClassXKnowledgeAndSkill> classXKnowledgeAndSkills = new LinkedHashSet<>();

  private Set<ProjectInputValue> projectInputValues = new LinkedHashSet<>();

  private Set<ProjectPostRating> projectPostRatings = new LinkedHashSet<>();

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

  @Lob
  @Column(name = COLUMN_TYPE_NAME, nullable = false)
  public String getType() {
    return type;
  }

  @Column(name = COLUMN_CATEGORY_NAME)
  public String getCategory() {
    return category;
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

  @Column(name = COLUMN_GLOBAL_NAME)
  public Boolean getGlobal() {
    return global;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_x_id", nullable = false)
  public UserX getUserX() {
    return userX;
  }

  @OneToMany(mappedBy = "knowledgeAndSkill")
  public Set<AssignmentKnowledgeAndSkill> getAssignmentKnowledgeAndSkills() {
    return assignmentKnowledgeAndSkills;
  }

  @OneToMany(mappedBy = "knowledgeAndSkill")
  public Set<ClassXKnowledgeAndSkill> getClassXKnowledgeAndSkills() {
    return classXKnowledgeAndSkills;
  }

  @OneToMany(mappedBy = "knowledgeAndSkillValue")
  public Set<ProjectInputValue> getProjectInputValues() {
    return projectInputValues;
  }

  @OneToMany(mappedBy = "knowledgeAndSkill")
  public Set<ProjectPostRating> getProjectPostRatings() {
    return projectPostRatings;
  }
}
