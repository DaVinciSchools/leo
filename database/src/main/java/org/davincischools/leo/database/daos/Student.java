package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Entity(name = Student.ENTITY_NAME)
@Table(name = Student.TABLE_NAME, schema = "leo_temp")
public class Student implements Serializable {

  public static final String ENTITY_NAME = "Student";
  public static final String TABLE_NAME = "student";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_DISTRICTSTUDENTID_NAME = "district_student_id";
  public static final String COLUMN_GRADE_NAME = "grade";
  private static final long serialVersionUID = -8061619706580471625L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private Integer districtStudentId;

  private Integer grade;

  private Set<StudentClassX> studentClassXES = new LinkedHashSet<>();

  private Set<StudentSchool> studentSchools = new LinkedHashSet<>();

  private UserX userX;

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

  @Column(name = COLUMN_DISTRICTSTUDENTID_NAME)
  public Integer getDistrictStudentId() {
    return districtStudentId;
  }

  @Column(name = COLUMN_GRADE_NAME)
  public Integer getGrade() {
    return grade;
  }

  @OneToMany(mappedBy = "student")
  public Set<StudentClassX> getStudentClassXES() {
    return studentClassXES;
  }

  @OneToMany(mappedBy = "student")
  public Set<StudentSchool> getStudentSchools() {
    return studentSchools;
  }

  @OneToOne(mappedBy = "student", fetch = jakarta.persistence.FetchType.LAZY)
  public UserX getUserX() {
    return userX;
  }
}
