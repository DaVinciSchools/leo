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
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Embeddable
public class StudentClassXId implements Serializable {

  public static final String COLUMN_STUDENTID_NAME = "student_id";
  public static final String COLUMN_CLASSXID_NAME = "class_x_id";
  private static final long serialVersionUID = -521514334135328966L;

  private Integer studentId;

  private Integer classXId;

  @Column(name = COLUMN_STUDENTID_NAME, nullable = false)
  public Integer getStudentId() {
    return studentId;
  }

  @Column(name = COLUMN_CLASSXID_NAME, nullable = false)
  public Integer getClassXId() {
    return classXId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getDaoClass(this) != getDaoClass(o)) return false;
    StudentClassXId entity = (StudentClassXId) o;
    return Objects.equals(this.studentId, entity.studentId)
        && Objects.equals(this.classXId, entity.classXId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, classXId);
  }
}
