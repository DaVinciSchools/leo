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

@Entity(name = TeacherSchool.ENTITY_NAME)
@Table(name = TeacherSchool.TABLE_NAME, schema = "leo_temp")
public class TeacherSchool implements Serializable {

  public static final String ENTITY_NAME = "TeacherSchool";
  public static final String TABLE_NAME = "teacher__school";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  private static final long serialVersionUID = 3880196199101576570L;

  private TeacherSchoolId id;

  private Teacher teacher;

  private School school;

  private Instant creationTime;

  private Instant deleted;

  @EmbeddedId
  public TeacherSchoolId getId() {
    return id;
  }

  public TeacherSchool setId(TeacherSchoolId id) {
    this.id = id;
    return this;
  }

  @MapsId("teacherId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "teacher_id", nullable = false)
  public Teacher getTeacher() {
    return teacher;
  }

  public TeacherSchool setTeacher(Teacher teacher) {
    this.teacher = teacher;
    return this;
  }

  @MapsId("schoolId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "school_id", nullable = false)
  public School getSchool() {
    return school;
  }

  public TeacherSchool setSchool(School school) {
    this.school = school;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public TeacherSchool setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  public TeacherSchool setDeleted(Instant deleted) {
    this.deleted = deleted;
    return this;
  }
}
