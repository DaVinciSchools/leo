package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.Hibernate;

@Embeddable
public class TeacherClassXId implements Serializable {

  public static final String COLUMN_TEACHERID_NAME = "teacher_id";
  public static final String COLUMN_CLASSXID_NAME = "class_x_id";
  private static final long serialVersionUID = -6064304844250562408L;

  private Integer teacherId;

  private Integer classXId;

  @Column(name = COLUMN_TEACHERID_NAME, nullable = false)
  public Integer getTeacherId() {
    return teacherId;
  }

  public TeacherClassXId setTeacherId(Integer teacherId) {
    this.teacherId = teacherId;
    return this;
  }

  @Column(name = COLUMN_CLASSXID_NAME, nullable = false)
  public Integer getClassXId() {
    return classXId;
  }

  public TeacherClassXId setClassXId(Integer classXId) {
    this.classXId = classXId;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    TeacherClassXId entity = (TeacherClassXId) o;
    return Objects.equals(this.teacherId, entity.teacherId)
        && Objects.equals(this.classXId, entity.classXId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(teacherId, classXId);
  }
}
