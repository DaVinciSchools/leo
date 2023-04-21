package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity(name = AdminX.ENTITY_NAME)
@Table(name = AdminX.TABLE_NAME, schema = "leo_temp")
public class AdminX {

  public static final String ENTITY_NAME = "AdminX";
  public static final String TABLE_NAME = "admin_x";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";

  private Integer id;

  private Instant creationTime;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public AdminX setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public AdminX setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }
}
