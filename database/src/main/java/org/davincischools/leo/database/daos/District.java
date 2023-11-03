package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
  private static final long serialVersionUID = -8914543911571870647L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String name;

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

  @OneToMany(mappedBy = "district")
  public Set<School> getSchools() {
    return schools;
  }

  @OneToMany(mappedBy = "district")
  public Set<UserX> getUserXES() {
    return userXES;
  }
}
