package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity(name = StudentSchool.ENTITY_NAME)
@Table(name = StudentSchool.TABLE_NAME, schema = "leo_temp")
public class StudentSchool implements Serializable {

  public static final String ENTITY_NAME = "StudentSchool";
  public static final String TABLE_NAME = "student__school";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  private static final long serialVersionUID = 3391692668117129125L;

  private StudentSchoolId id;

  private Student student;

  private School school;

  private Instant creationTime;

  @EmbeddedId
  public StudentSchoolId getId() {
    return id;
  }

  public StudentSchool setId(StudentSchoolId id) {
    this.id = id;
    return this;
  }

  @MapsId("studentId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "student_id", nullable = false)
  public Student getStudent() {
    return student;
  }

  public StudentSchool setStudent(Student student) {
    this.student = student;
    return this;
  }

  @MapsId("schoolId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "school_id", nullable = false)
  public School getSchool() {
    return school;
  }

  public StudentSchool setSchool(School school) {
    this.school = school;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public StudentSchool setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }
}
