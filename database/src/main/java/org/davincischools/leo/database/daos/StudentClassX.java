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

@Entity(name = StudentClassX.ENTITY_NAME)
@Table(name = StudentClassX.TABLE_NAME, schema = "leo_temp")
public class StudentClassX implements Serializable {

  public static final String ENTITY_NAME = "StudentClassX";
  public static final String TABLE_NAME = "student__class_x";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  private static final long serialVersionUID = 4635943641230716033L;

  private StudentClassXId id;

  private Student student;

  private ClassX classX;

  private Instant creationTime;

  @EmbeddedId
  public StudentClassXId getId() {
    return id;
  }

  public StudentClassX setId(StudentClassXId id) {
    this.id = id;
    return this;
  }

  @MapsId("studentId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "student_id", nullable = false)
  public Student getStudent() {
    return student;
  }

  public StudentClassX setStudent(Student student) {
    this.student = student;
    return this;
  }

  @MapsId("classXId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "class_x_id", nullable = false)
  public ClassX getClassX() {
    return classX;
  }

  public StudentClassX setClassX(ClassX classX) {
    this.classX = classX;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public StudentClassX setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }
}
