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
@Entity(name = StudentClassX.ENTITY_NAME)
@Table(name = StudentClassX.TABLE_NAME, schema = "leo_test")
public class StudentClassX implements Serializable {

  public static final String ENTITY_NAME = "StudentClassX";
  public static final String TABLE_NAME = "student__class_x";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  @Serial private static final long serialVersionUID = -8229916131994266256L;

  private StudentClassXId id;

  private Student student;

  private ClassX classX;

  private Instant creationTime;

  private Instant deleted;

  @EmbeddedId
  public StudentClassXId getId() {
    return id;
  }

  @MapsId("studentId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "student_id", nullable = false)
  @PropagateDeleteFrom
  public Student getStudent() {
    return student;
  }

  @MapsId("classXId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "class_x_id", nullable = false)
  @PropagateDeleteFrom
  public ClassX getClassX() {
    return classX;
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
