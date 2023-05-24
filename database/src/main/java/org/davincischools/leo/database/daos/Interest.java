package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity(name = Interest.ENTITY_NAME)
@Table(name = Interest.TABLE_NAME, schema = "leo_temp")
public class Interest implements Serializable {

  public static final String ENTITY_NAME = "Interest";
  public static final String TABLE_NAME = "interest";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
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
  private static final long serialVersionUID = 1089097917992088909L;

  private Integer id;

  private Instant creationTime;

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

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public Interest setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public Interest setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_FIRSTNAME_NAME, nullable = false)
  public String getFirstName() {
    return firstName;
  }

  public Interest setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  @Column(name = COLUMN_LASTNAME_NAME, nullable = false)
  public String getLastName() {
    return lastName;
  }

  public Interest setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  @Column(name = COLUMN_EMAILADDRESS_NAME, nullable = false, length = 254)
  public String getEmailAddress() {
    return emailAddress;
  }

  public Interest setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

  @Column(name = COLUMN_PROFESSION_NAME, nullable = false)
  public String getProfession() {
    return profession;
  }

  public Interest setProfession(String profession) {
    this.profession = profession;
    return this;
  }

  @Lob
  @Column(name = COLUMN_REASONFORINTEREST_NAME, nullable = false)
  public String getReasonForInterest() {
    return reasonForInterest;
  }

  public Interest setReasonForInterest(String reasonForInterest) {
    this.reasonForInterest = reasonForInterest;
    return this;
  }

  @Column(name = COLUMN_DISTRICTNAME_NAME)
  public String getDistrictName() {
    return districtName;
  }

  public Interest setDistrictName(String districtName) {
    this.districtName = districtName;
    return this;
  }

  @Column(name = COLUMN_SCHOOLNAME_NAME)
  public String getSchoolName() {
    return schoolName;
  }

  public Interest setSchoolName(String schoolName) {
    this.schoolName = schoolName;
    return this;
  }

  @Column(name = COLUMN_ADDRESSLINE1_NAME)
  public String getAddressLine1() {
    return addressLine1;
  }

  public Interest setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
    return this;
  }

  @Column(name = COLUMN_ADDRESSLINE2_NAME)
  public String getAddressLine2() {
    return addressLine2;
  }

  public Interest setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
    return this;
  }

  @Column(name = COLUMN_CITY_NAME, length = 20)
  public String getCity() {
    return city;
  }

  public Interest setCity(String city) {
    this.city = city;
    return this;
  }

  @Column(name = COLUMN_STATE_NAME, length = 2)
  public String getState() {
    return state;
  }

  public Interest setState(String state) {
    this.state = state;
    return this;
  }

  @Column(name = COLUMN_ZIPCODE_NAME, length = 10)
  public String getZipCode() {
    return zipCode;
  }

  public Interest setZipCode(String zipCode) {
    this.zipCode = zipCode;
    return this;
  }

  @Column(name = COLUMN_NUMTEACHERS_NAME)
  public Integer getNumTeachers() {
    return numTeachers;
  }

  public Interest setNumTeachers(Integer numTeachers) {
    this.numTeachers = numTeachers;
    return this;
  }

  @Column(name = COLUMN_NUMSTUDENTS_NAME)
  public Integer getNumStudents() {
    return numStudents;
  }

  public Interest setNumStudents(Integer numStudents) {
    this.numStudents = numStudents;
    return this;
  }
}
