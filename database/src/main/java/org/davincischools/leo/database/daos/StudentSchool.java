package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.davincischools.leo.database.dao_interfaces.PropagateDeleteFrom;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity(name = StudentSchool.ENTITY_NAME)
@Table(name = StudentSchool.TABLE_NAME, schema = "leo_test")
public class StudentSchool implements Serializable {

  public static final String ENTITY_NAME = "StudentSchool";
  public static final String TABLE_NAME = "student__school";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  @Serial private static final long serialVersionUID = -7583637380013716130L;

  private StudentSchoolId id;

  private Student student;

  private School school;

  private Instant creationTime;

  private Instant deleted;

  @EmbeddedId
  public StudentSchoolId getId() {
    return id;
  }

  @MapsId("studentId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "student_id", nullable = false)
  @PropagateDeleteFrom
  public Student getStudent() {
    return student;
  }

  @MapsId("schoolId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "school_id", nullable = false)
  @PropagateDeleteFrom
  public School getSchool() {
    return school;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }
}
