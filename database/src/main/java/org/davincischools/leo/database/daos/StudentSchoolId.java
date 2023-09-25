package org.davincischools.leo.database.daos;

import static org.davincischools.leo.database.utils.DaoUtils.getDaoClass;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
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
@Embeddable
public class StudentSchoolId implements Serializable {

  public static final String COLUMN_STUDENTID_NAME = "student_id";
  public static final String COLUMN_SCHOOLID_NAME = "school_id";
  private static final long serialVersionUID = -3379642978690408391L;

  private Integer studentId;

  private Integer schoolId;

  @Column(name = COLUMN_STUDENTID_NAME, nullable = false)
  public Integer getStudentId() {
    return studentId;
  }

  @Column(name = COLUMN_SCHOOLID_NAME, nullable = false)
  public Integer getSchoolId() {
    return schoolId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getDaoClass(this) != getDaoClass(o)) return false;
    StudentSchoolId entity = (StudentSchoolId) o;
    return Objects.equals(this.studentId, entity.studentId)
        && Objects.equals(this.schoolId, entity.schoolId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, schoolId);
  }
}
