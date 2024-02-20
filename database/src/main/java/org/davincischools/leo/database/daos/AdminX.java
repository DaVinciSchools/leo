package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
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
@Entity(name = AdminX.ENTITY_NAME)
@Table(name = AdminX.TABLE_NAME, schema = "leo_test")
public class AdminX implements Serializable {

  public static final String ENTITY_NAME = "AdminX";
  public static final String TABLE_NAME = "admin_x";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_ISCROSSDISTRICTADMINX_NAME = "is_cross_district_admin_x";
  @Serial private static final long serialVersionUID = 964556327157095297L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private Boolean isCrossDistrictAdminX;

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

  @Column(name = COLUMN_ISCROSSDISTRICTADMINX_NAME)
  public Boolean getIsCrossDistrictAdminX() {
    return isCrossDistrictAdminX;
  }

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "adminX")
  public UserX getUserX() {
    return userX;
  }
}
