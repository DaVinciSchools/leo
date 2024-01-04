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
import org.davincischools.leo.database.dao_interfaces.PropagateDeleteFrom;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity(name = ClassX.ENTITY_NAME)
@Table(name = ClassX.TABLE_NAME, schema = "leo_test")
public class ClassX implements Serializable {

  public static final String ENTITY_NAME = "ClassX";
  public static final String TABLE_NAME = "class_x";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_NUMBER_NAME = "number";
  public static final String COLUMN_PERIOD_NAME = "period";
  public static final String COLUMN_GRADE_NAME = "grade";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_LONGDESCRHTML_NAME = "long_descr_html";
  private static final long serialVersionUID = -5488875829747816021L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String name;

  private String number;

  private String period;

  private String grade;

  private String shortDescr;

  private String longDescrHtml;

  private School school;

  private Set<Assignment> assignments = new LinkedHashSet<>();

  private Set<ClassXKnowledgeAndSkill> classXKnowledgeAndSkills = new LinkedHashSet<>();

  private Set<StudentClassX> studentClassXES = new LinkedHashSet<>();

  private Set<TeacherClassX> teacherClassXES = new LinkedHashSet<>();

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

  @Column(name = COLUMN_NUMBER_NAME, nullable = false)
  public String getNumber() {
    return number;
  }

  @Column(name = COLUMN_PERIOD_NAME, length = 16)
  public String getPeriod() {
    return period;
  }

  @Column(name = COLUMN_GRADE_NAME, length = 16)
  public String getGrade() {
    return grade;
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
  @JoinColumn(name = "school_id")
  @PropagateDeleteFrom
  public School getSchool() {
    return school;
  }

  @OneToMany(mappedBy = "classX")
  public Set<Assignment> getAssignments() {
    return assignments;
  }

  @OneToMany(mappedBy = "classX")
  public Set<ClassXKnowledgeAndSkill> getClassXKnowledgeAndSkills() {
    return classXKnowledgeAndSkills;
  }

  @OneToMany(mappedBy = "classX")
  public Set<StudentClassX> getStudentClassXES() {
    return studentClassXES;
  }

  @OneToMany(mappedBy = "classX")
  public Set<TeacherClassX> getTeacherClassXES() {
    return teacherClassXES;
  }
}
