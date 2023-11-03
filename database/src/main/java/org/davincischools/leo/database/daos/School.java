package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity(name = School.ENTITY_NAME)
@Table(
    name = School.TABLE_NAME,
    schema = "leo_test",
    indexes = {
      @Index(
          name = "school__district__unique_name",
          columnList = "name, district_id",
          unique = true),
      @Index(
          name = "school__district__unique_nickname",
          columnList = "nickname, district_id",
          unique = true)
    })
public class School implements Serializable {

  public static final String ENTITY_NAME = "School";
  public static final String TABLE_NAME = "school";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_NICKNAME_NAME = "nickname";
  public static final String COLUMN_ADDRESS_NAME = "address";
  private static final long serialVersionUID = 4147549771007920763L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String name;

  private String nickname;

  private String address;

  private District district;

  private Set<ClassX> classXES = new LinkedHashSet<>();

  private Set<StudentSchool> studentSchools = new LinkedHashSet<>();

  private Set<TeacherSchool> teacherSchools = new LinkedHashSet<>();

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

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  @Column(name = COLUMN_NICKNAME_NAME)
  public String getNickname() {
    return nickname;
  }

  @Column(name = COLUMN_ADDRESS_NAME)
  public String getAddress() {
    return address;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "district_id", nullable = false)
  public District getDistrict() {
    return district;
  }

  @OneToMany(mappedBy = "school")
  public Set<ClassX> getClassXES() {
    return classXES;
  }

  @OneToMany(mappedBy = "school")
  public Set<StudentSchool> getStudentSchools() {
    return studentSchools;
  }

  @OneToMany(mappedBy = "school")
  public Set<TeacherSchool> getTeacherSchools() {
    return teacherSchools;
  }
}
