package org.davincischools.leo.database.daos;

import static org.davincischools.leo.database.utils.DaoUtils.getDaoClass;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Embeddable
public class TeacherSchoolId implements Serializable {

  public static final String COLUMN_TEACHERID_NAME = "teacher_id";
  public static final String COLUMN_SCHOOLID_NAME = "school_id";
  @Serial private static final long serialVersionUID = 2771265595447998581L;

  private Integer teacherId;

  private Integer schoolId;

  @Column(name = COLUMN_TEACHERID_NAME, nullable = false)
  public Integer getTeacherId() {
    return teacherId;
  }

  @Column(name = COLUMN_SCHOOLID_NAME, nullable = false)
  public Integer getSchoolId() {
    return schoolId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getDaoClass(this) != getDaoClass(o)) return false;
    TeacherSchoolId entity = (TeacherSchoolId) o;
    return Objects.equals(this.teacherId, entity.teacherId)
        && Objects.equals(this.schoolId, entity.schoolId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(teacherId, schoolId);
  }
}
