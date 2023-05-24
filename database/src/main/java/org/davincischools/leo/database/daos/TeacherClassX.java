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

@Entity(name = TeacherClassX.ENTITY_NAME)
@Table(name = TeacherClassX.TABLE_NAME, schema = "leo_temp")
public class TeacherClassX implements Serializable {

  public static final String ENTITY_NAME = "TeacherClassX";
  public static final String TABLE_NAME = "teacher__class_x";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  private static final long serialVersionUID = -1748924832116102991L;

  private TeacherClassXId id;

  private Teacher teacher;

  private ClassX classX;

  private Instant creationTime;

  @EmbeddedId
  public TeacherClassXId getId() {
    return id;
  }

  public TeacherClassX setId(TeacherClassXId id) {
    this.id = id;
    return this;
  }

  @MapsId("teacherId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "teacher_id", nullable = false)
  public Teacher getTeacher() {
    return teacher;
  }

  public TeacherClassX setTeacher(Teacher teacher) {
    this.teacher = teacher;
    return this;
  }

  @MapsId("classXId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "class_x_id", nullable = false)
  public ClassX getClassX() {
    return classX;
  }

  public TeacherClassX setClassX(ClassX classX) {
    this.classX = classX;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public TeacherClassX setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }
}
