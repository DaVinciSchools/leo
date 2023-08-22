package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.EntityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserXRepository extends JpaRepository<UserX, Integer> {

  public static final int MAX_EMAIL_ADDRESS_LENGTH =
      EntityUtils.getColumn(UserX.class, UserX.COLUMN_EMAILADDRESS_NAME).length();
  public static final int MAX_FIRST_NAME_LENGTH =
      EntityUtils.getColumn(UserX.class, UserX.COLUMN_FIRSTNAME_NAME).length();
  public static final int MAX_LAST_NAME_LENGTH =
      EntityUtils.getColumn(UserX.class, UserX.COLUMN_LASTNAME_NAME).length();

  public static final int MIN_PASSWORD_LENGTH = 8;
  public static final String INVALID_ENCODED_PASSWORD = "INVALID ENCODED PASSWORD";

  enum Role {
    ADMIN_X,
    TEACHER,
    STUDENT,
    DEMO
  }

  static boolean isAdminX(UserX userX) {
    return userX.getAdminX() != null && userX.getAdminX().getId() != null;
  }

  static boolean isTeacher(UserX userX) {
    return userX.getTeacher() != null && userX.getTeacher().getId() != null;
  }

  static boolean isStudent(UserX userX) {
    return userX.getStudent() != null && userX.getStudent().getId() != null;
  }

  static boolean isDemo(UserX userX) {
    return isAuthenticated(userX) && !isAdminX(userX) && !isTeacher(userX) && !isStudent(userX);
  }

  static boolean isAuthenticated(UserX userX) {
    return userX.getId() != null;
  }

  static EnumSet<Role> getRoles(UserX user) {
    checkNotNull(user);

    EnumSet<Role> roles = EnumSet.noneOf(Role.class);
    if (isAdminX(user)) {
      roles.add(Role.ADMIN_X);
    }
    if (isTeacher(user)) {
      roles.add(Role.TEACHER);
    }
    if (isStudent(user)) {
      roles.add(Role.STUDENT);
    }
    if (isDemo(user)) {
      roles.add(Role.DEMO);
    }
    return roles;
  }

  default UserX upsert(District district, String emailAddress, Consumer<UserX> modifier) {
    checkArgument(!Strings.isNullOrEmpty(emailAddress));
    checkNotNull(modifier);

    UserX userX =
        findByEmailAddress(emailAddress)
            .orElseGet(
                () ->
                    new UserX()
                        .setCreationTime(Instant.now())
                        .setFirstName("First Name")
                        .setLastName("Last Name")
                        .setEncodedPassword(INVALID_ENCODED_PASSWORD))
            .setDistrict(district)
            .setEmailAddress(emailAddress);

    modifier.accept(userX);

    return saveAndFlush(userX);
  }

  @Query(
      "SELECT u FROM UserX u"
          + " LEFT JOIN FETCH u.district"
          + " LEFT JOIN FETCH u.adminX"
          + " LEFT JOIN FETCH u.teacher"
          + " LEFT JOIN FETCH u.student"
          + " WHERE u.id = (:userXId)")
  Optional<UserX> findById(@Param("userXId") int userXId);

  @Query(
      "SELECT u FROM UserX u"
          + " LEFT JOIN FETCH u.district"
          + " LEFT JOIN FETCH u.adminX"
          + " LEFT JOIN FETCH u.teacher"
          + " LEFT JOIN FETCH u.student"
          + " WHERE u.teacher.id = (:teacherId)")
  Optional<UserX> findByTeacherId(@Param("teacherId") int teacherId);

  @Query(
      "SELECT u FROM UserX u"
          + " LEFT JOIN FETCH u.district"
          + " LEFT JOIN FETCH u.adminX"
          + " LEFT JOIN FETCH u.teacher"
          + " LEFT JOIN FETCH u.student"
          + " WHERE u.emailAddress = (:emailAddress)")
  Optional<UserX> findByEmailAddress(@Param("emailAddress") String emailAddress);

  @Query(
      "SELECT u FROM UserX u"
          + " LEFT JOIN FETCH u.district"
          + " LEFT JOIN FETCH u.adminX"
          + " LEFT JOIN FETCH u.teacher"
          + " LEFT JOIN FETCH u.student"
          + " WHERE u.district.id = (:districtId)")
  List<UserX> findAllByDistrictId(@Param("districtId") int districtId);

  @Query(
      "SELECT u FROM UserX u"
          + " LEFT JOIN FETCH u.district"
          + " LEFT JOIN FETCH u.adminX"
          + " LEFT JOIN FETCH u.teacher"
          + " LEFT JOIN FETCH u.student"
          + " WHERE u.district.id = (:districtId)"
          + " AND ("
          + "    LOWER(u.firstName) LIKE (:searchText) "
          + " OR LOWER(u.lastName) LIKE (:searchText) "
          + " OR LOWER(u.emailAddress) LIKE (:searchText))")
  Page<UserX> findAllByDistrictId(
      @Param("districtId") int districtId,
      @Param("searchText") String searchText,
      Pageable pageable);
}
