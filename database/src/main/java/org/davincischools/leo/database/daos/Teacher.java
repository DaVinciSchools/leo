package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
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
@Entity(name = Teacher.ENTITY_NAME)
@Table(name = Teacher.TABLE_NAME, schema = "leo_test")
public class Teacher implements Serializable {

  public static final String ENTITY_NAME = "Teacher";
  public static final String TABLE_NAME = "teacher";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  private static final long serialVersionUID = -8690567610954390964L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private Set<TeacherClassX> teacherClassXES = new LinkedHashSet<>();

  private Set<TeacherSchool> teacherSchools = new LinkedHashSet<>();

  private UserX userX;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  @OneToMany(mappedBy = "teacher")
  public Set<TeacherClassX> getTeacherClassXES() {
    return teacherClassXES;
  }

  @OneToMany(mappedBy = "teacher")
  public Set<TeacherSchool> getTeacherSchools() {
    return teacherSchools;
  }

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "teacher")
  public UserX getUserX() {
    return userX;
  }
}
