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
import org.davincischools.leo.database.dao_interfaces.PropagateDeleteTo;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity(name = UserX.ENTITY_NAME)
@Table(
    name = UserX.TABLE_NAME,
    schema = "leo_test",
    indexes = {
      @Index(name = "email_address", columnList = "email_address", unique = true),
      @Index(name = "admin_x_id", columnList = "admin_x_id", unique = true),
      @Index(name = "teacher_id", columnList = "teacher_id", unique = true),
      @Index(name = "student_id", columnList = "student_id", unique = true),
      @Index(name = "interest_id", columnList = "interest_id", unique = true),
      @Index(name = "user_x__email_address", columnList = "email_address")
    })
public class UserX implements Serializable {

  public static final String ENTITY_NAME = "UserX";
  public static final String TABLE_NAME = "user_x";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_FIRSTNAME_NAME = "first_name";
  public static final String COLUMN_LASTNAME_NAME = "last_name";
  public static final String COLUMN_EMAILADDRESS_NAME = "email_address";
  public static final String COLUMN_EMAILADDRESSVERIFIED_NAME = "email_address_verified";
  public static final String COLUMN_AVATARIMAGEURL_NAME = "avatar_image_url";
  public static final String COLUMN_ENCODEDPASSWORD_NAME = "encoded_password";
  public static final String COLUMN_TEMPORARYPASSWORDGOODUNTIL_NAME =
      "temporary_password_good_until";
  public static final String COLUMN_TEMPORARYENCODEDPASSWORD_NAME = "temporary_encoded_password";
  private static final long serialVersionUID = 5145772325639569618L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String firstName;

  private String lastName;

  private String emailAddress;

  private Boolean emailAddressVerified;

  private String avatarImageUrl;

  private String encodedPassword;

  private Instant temporaryPasswordGoodUntil;

  private String temporaryEncodedPassword;

  private District district;

  private AdminX adminX;

  private Teacher teacher;

  private Student student;

  private Interest interest;

  private Set<FileX> fileXES = new LinkedHashSet<>();

  private Set<KnowledgeAndSkill> knowledgeAndSkills = new LinkedHashSet<>();

  private Set<Log> logs = new LinkedHashSet<>();

  private Set<ProjectDefinition> projectDefinitions = new LinkedHashSet<>();

  private Set<ProjectInput> projectInputs = new LinkedHashSet<>();

  private Set<ProjectPost> projectPosts = new LinkedHashSet<>();

  private Set<ProjectPostComment> projectPostComments = new LinkedHashSet<>();

  private Set<ProjectPostRating> projectPostRatings = new LinkedHashSet<>();

  private Set<Tag> tags = new LinkedHashSet<>();

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

  @Column(name = COLUMN_EMAILADDRESSVERIFIED_NAME)
  public Boolean getEmailAddressVerified() {
    return emailAddressVerified;
  }

  @Column(name = COLUMN_AVATARIMAGEURL_NAME)
  public String getAvatarImageUrl() {
    return avatarImageUrl;
  }

  @Lob
  @Column(name = COLUMN_ENCODEDPASSWORD_NAME)
  public String getEncodedPassword() {
    return encodedPassword;
  }

  @Column(name = COLUMN_TEMPORARYPASSWORDGOODUNTIL_NAME)
  public Instant getTemporaryPasswordGoodUntil() {
    return temporaryPasswordGoodUntil;
  }

  @Lob
  @Column(name = COLUMN_TEMPORARYENCODEDPASSWORD_NAME)
  public String getTemporaryEncodedPassword() {
    return temporaryEncodedPassword;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "district_id")
  public District getDistrict() {
    return district;
  }

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "admin_x_id")
  @PropagateDeleteTo
  public AdminX getAdminX() {
    return adminX;
  }

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teacher_id")
  @PropagateDeleteTo
  public Teacher getTeacher() {
    return teacher;
  }

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "student_id")
  @PropagateDeleteTo
  public Student getStudent() {
    return student;
  }

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "interest_id")
  public Interest getInterest() {
    return interest;
  }

  @OneToMany(mappedBy = "userX")
  public Set<FileX> getFileXES() {
    return fileXES;
  }

  @OneToMany(mappedBy = "userX")
  public Set<KnowledgeAndSkill> getKnowledgeAndSkills() {
    return knowledgeAndSkills;
  }

  @OneToMany(mappedBy = "userX")
  public Set<Log> getLogs() {
    return logs;
  }

  @OneToMany(mappedBy = "userX")
  public Set<ProjectDefinition> getProjectDefinitions() {
    return projectDefinitions;
  }

  @OneToMany(mappedBy = "userX")
  public Set<ProjectInput> getProjectInputs() {
    return projectInputs;
  }

  @OneToMany(mappedBy = "userX")
  public Set<ProjectPost> getProjectPosts() {
    return projectPosts;
  }

  @OneToMany(mappedBy = "userX")
  public Set<ProjectPostComment> getProjectPostComments() {
    return projectPostComments;
  }

  @OneToMany(mappedBy = "userX")
  public Set<ProjectPostRating> getProjectPostRatings() {
    return projectPostRatings;
  }

  @OneToMany(mappedBy = "userX")
  public Set<Tag> getTags() {
    return tags;
  }
}
