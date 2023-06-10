package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity(name = Student.ENTITY_NAME)
@Table(name = Student.TABLE_NAME, schema = "leo_temp")
public class Student implements Serializable {

  public static final String ENTITY_NAME = "Student";
  public static final String TABLE_NAME = "student";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_STUDENTID_NAME = "student_id";
  public static final String COLUMN_GRADE_NAME = "grade";
  private static final long serialVersionUID = 6486876479287914154L;

  private Integer id;

  private Instant creationTime;

  private Integer studentId;

  private Integer grade;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public Student setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public Student setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_STUDENTID_NAME)
  public Integer getStudentId() {
    return studentId;
  }

  public Student setStudentId(Integer studentId) {
    this.studentId = studentId;
    return this;
  }

  @Column(name = COLUMN_GRADE_NAME)
  public Integer getGrade() {
    return grade;
  }

  public Student setGrade(Integer grade) {
    this.grade = grade;
    return this;
  }
}
