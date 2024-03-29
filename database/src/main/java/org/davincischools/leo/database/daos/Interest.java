package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
@Entity(name = Interest.ENTITY_NAME)
@Table(name = Interest.TABLE_NAME, schema = "leo_test")
public class Interest implements Serializable {

  public static final String ENTITY_NAME = "Interest";
  public static final String TABLE_NAME = "interest";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_FIRSTNAME_NAME = "first_name";
  public static final String COLUMN_LASTNAME_NAME = "last_name";
  public static final String COLUMN_EMAILADDRESS_NAME = "email_address";
  public static final String COLUMN_PROFESSION_NAME = "profession";
  public static final String COLUMN_REASONFORINTEREST_NAME = "reason_for_interest";
  public static final String COLUMN_DISTRICTNAME_NAME = "district_name";
  public static final String COLUMN_SCHOOLNAME_NAME = "school_name";
  public static final String COLUMN_ADDRESSLINE1_NAME = "address_line_1";
  public static final String COLUMN_ADDRESSLINE2_NAME = "address_line_2";
  public static final String COLUMN_CITY_NAME = "city";
  public static final String COLUMN_STATE_NAME = "state";
  public static final String COLUMN_ZIPCODE_NAME = "zip_code";
  public static final String COLUMN_NUMTEACHERS_NAME = "num_teachers";
  public static final String COLUMN_NUMSTUDENTS_NAME = "num_students";
  @Serial private static final long serialVersionUID = -7584579189306687225L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String firstName;

  private String lastName;

  private String emailAddress;

  private String profession;

  private String reasonForInterest;

  private String districtName;

  private String schoolName;

  private String addressLine1;

  private String addressLine2;

  private String city;

  private String state;

  private String zipCode;

  private Integer numTeachers;

  private Integer numStudents;

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

  @Column(name = COLUMN_FIRSTNAME_NAME, nullable = false)
  public String getFirstName() {
    return firstName;
  }

  @Column(name = COLUMN_LASTNAME_NAME, nullable = false)
  public String getLastName() {
    return lastName;
  }

  @Column(name = COLUMN_EMAILADDRESS_NAME, nullable = false, length = 254)
  public String getEmailAddress() {
    return emailAddress;
  }

  @Column(name = COLUMN_PROFESSION_NAME, nullable = false)
  public String getProfession() {
    return profession;
  }

  @Lob
  @Column(name = COLUMN_REASONFORINTEREST_NAME, nullable = false)
  public String getReasonForInterest() {
    return reasonForInterest;
  }

  @Column(name = COLUMN_DISTRICTNAME_NAME)
  public String getDistrictName() {
    return districtName;
  }

  @Column(name = COLUMN_SCHOOLNAME_NAME)
  public String getSchoolName() {
    return schoolName;
  }

  @Column(name = COLUMN_ADDRESSLINE1_NAME)
  public String getAddressLine1() {
    return addressLine1;
  }

  @Column(name = COLUMN_ADDRESSLINE2_NAME)
  public String getAddressLine2() {
    return addressLine2;
  }

  @Column(name = COLUMN_CITY_NAME, length = 20)
  public String getCity() {
    return city;
  }

  @Column(name = COLUMN_STATE_NAME, length = 2)
  public String getState() {
    return state;
  }

  @Column(name = COLUMN_ZIPCODE_NAME, length = 10)
  public String getZipCode() {
    return zipCode;
  }

  @Column(name = COLUMN_NUMTEACHERS_NAME)
  public Integer getNumTeachers() {
    return numTeachers;
  }

  @Column(name = COLUMN_NUMSTUDENTS_NAME)
  public Integer getNumStudents() {
    return numStudents;
  }

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "interest")
  public UserX getUserX() {
    return userX;
  }
}
