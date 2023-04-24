package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity(name = School.ENTITY_NAME)
@Table(name = School.TABLE_NAME, schema = "leo_temp")
public class School {

  public static final String ENTITY_NAME = "School";
  public static final String TABLE_NAME = "school";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_ADDRESS_NAME = "address";

  private Integer id;

  private Instant creationTime;

  private String name;

  private String address;

  private District district;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public School setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public School setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public School setName(String name) {
    this.name = name;
    return this;
  }

  @Column(name = COLUMN_ADDRESS_NAME, nullable = false)
  public String getAddress() {
    return address;
  }

  public School setAddress(String address) {
    this.address = address;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "district_id", nullable = false)
  public District getDistrict() {
    return district;
  }

  public School setDistrict(District district) {
    this.district = district;
    return this;
  }
}
