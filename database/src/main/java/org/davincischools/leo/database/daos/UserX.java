package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity(name = UserX.ENTITY_NAME)
@Table(
    name = UserX.TABLE_NAME,
    schema = "leo_temp",
    indexes = {
      @Index(name = "email_address", columnList = "email_address", unique = true),
      @Index(name = "admin_x_id", columnList = "admin_x_id", unique = true),
      @Index(name = "teacher_id", columnList = "teacher_id", unique = true),
      @Index(name = "student_id", columnList = "student_id", unique = true),
      @Index(name = "user_x__email_address", columnList = "email_address")
    })
public class UserX implements Serializable {

  public static final String ENTITY_NAME = "UserX";
  public static final String TABLE_NAME = "user_x";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_FIRSTNAME_NAME = "first_name";
  public static final String COLUMN_LASTNAME_NAME = "last_name";
  public static final String COLUMN_EMAILADDRESS_NAME = "email_address";
  public static final String COLUMN_ENCODEDPASSWORD_NAME = "encoded_password";
  private static final long serialVersionUID = 1623331932844911934L;

  private Integer id;

  private Instant creationTime;

  private String firstName;

  private String lastName;

  private String emailAddress;

  private String encodedPassword;

  private District district;

  private AdminX adminX;

  private Teacher teacher;

  private Student student;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public UserX setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public UserX setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_FIRSTNAME_NAME, nullable = false)
  public String getFirstName() {
    return firstName;
  }

  public UserX setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  @Column(name = COLUMN_LASTNAME_NAME, nullable = false)
  public String getLastName() {
    return lastName;
  }

  public UserX setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  @Column(name = COLUMN_EMAILADDRESS_NAME, nullable = false, length = 254)
  public String getEmailAddress() {
    return emailAddress;
  }

  public UserX setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

  @Lob
  @Column(name = COLUMN_ENCODEDPASSWORD_NAME, nullable = false)
  public String getEncodedPassword() {
    return encodedPassword;
  }

  public UserX setEncodedPassword(String encodedPassword) {
    this.encodedPassword = encodedPassword;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "district_id")
  public District getDistrict() {
    return district;
  }

  public UserX setDistrict(District district) {
    this.district = district;
    return this;
  }

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "admin_x_id")
  public AdminX getAdminX() {
    return adminX;
  }

  public UserX setAdminX(AdminX adminX) {
    this.adminX = adminX;
    return this;
  }

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teacher_id")
  public Teacher getTeacher() {
    return teacher;
  }

  public UserX setTeacher(Teacher teacher) {
    this.teacher = teacher;
    return this;
  }

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "student_id")
  public Student getStudent() {
    return student;
  }

  public UserX setStudent(Student student) {
    this.student = student;
    return this;
  }
}
