package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serial;
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
@Entity(name = District.ENTITY_NAME)
@Table(
    name = District.TABLE_NAME,
    schema = "leo_test",
    indexes = {@Index(name = "name", columnList = "name", unique = true)})
public class District implements Serializable {

  public static final String ENTITY_NAME = "District";
  public static final String TABLE_NAME = "district";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_ISDEMO_NAME = "is_demo";
  @Serial private static final long serialVersionUID = -6404012584007719358L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String name;

  private Boolean isDemo;

  private Set<School> schools = new LinkedHashSet<>();

  private Set<UserX> userXES = new LinkedHashSet<>();

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

  @Column(name = COLUMN_ISDEMO_NAME)
  public Boolean getIsDemo() {
    return isDemo;
  }

  @OneToMany(mappedBy = "district")
  public Set<School> getSchools() {
    return schools;
  }

  @OneToMany(mappedBy = "district")
  public Set<UserX> getUserXES() {
    return userXES;
  }
}
