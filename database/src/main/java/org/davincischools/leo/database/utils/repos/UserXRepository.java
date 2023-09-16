package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.davincischools.leo.database.daos.ClassX_;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.School_;
import org.davincischools.leo.database.daos.StudentClassX_;
import org.davincischools.leo.database.daos.StudentSchool_;
import org.davincischools.leo.database.daos.Student_;
import org.davincischools.leo.database.daos.TeacherClassX_;
import org.davincischools.leo.database.daos.TeacherSchool_;
import org.davincischools.leo.database.daos.Teacher_;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.daos.UserX_;
import org.davincischools.leo.database.utils.DaoUtils;
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

  @Setter
  @Getter
  @Accessors(chain = true)
  @NoArgsConstructor
  class GetUserXParams {

    @Nullable private Boolean includeAdminXs;
    @Nullable private Boolean includeTeachers;
    @Nullable private Boolean includeStudents;

    @Nullable private List<Integer> schoolIds;
    @Nullable private List<Integer> classXIds;
    @Nullable private String firstLastEmailSearchText;

    Optional<Boolean> getIncludeAdminXs() {
      return Optional.ofNullable(includeAdminXs);
    }

    Optional<Boolean> getIncludeTeachers() {
      return Optional.ofNullable(includeTeachers);
    }

    Optional<Boolean> getIncludeStudents() {
      return Optional.ofNullable(includeStudents);
    }

    Optional<List<Integer>> getSchoolIds() {
      return Optional.ofNullable(schoolIds);
    }

    Optional<List<Integer>> getClassXIds() {
      return Optional.ofNullable(classXIds);
    }

    Optional<String> getFirstLastEmailSearchText() {
      return Optional.ofNullable(firstLastEmailSearchText);
    }
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

  default List<UserX> getUserXs(EntityManager entityManager, GetUserXParams params) {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    // Set up tables.
    CriteriaQuery<Tuple> query = builder.createTupleQuery();
    List<Predicate> whereConjunctions = new ArrayList<>();
    List<Predicate> whereDisjunctions = new ArrayList<>();

    // FROM ProjectPost.
    Root<UserX> userX = query.from(UserX.class);
    userX.fetch(UserX_.adminX, JoinType.LEFT);
    userX.fetch(UserX_.teacher, JoinType.LEFT);
    userX.fetch(UserX_.student, JoinType.LEFT);

    // WHERE - General.
    whereConjunctions.add(builder.isNull(userX.get(UserX_.deleted)));

    // WHERE includeAdmins.
    if (params.getIncludeAdminXs().orElse(false)) {
      whereDisjunctions.add(builder.isNotNull(userX.get(UserX_.adminX)));
    }

    // WHERE includeTeachers.
    if (params.getIncludeTeachers().orElse(false)) {
      List<Predicate> includeConjunctions = new ArrayList<>();
      includeConjunctions.add(builder.isNotNull(userX.get(UserX_.teacher)));

      // WHERE includeTeachers schoolIds.
      if (params.getSchoolIds().isPresent() && !params.getSchoolIds().isEmpty()) {
        var inPredicate =
            builder.in(
                DaoUtils.toJoin(
                        userX
                            .fetch(UserX_.teacher, JoinType.LEFT)
                            .fetch(Teacher_.teacherSchools, JoinType.LEFT))
                    .get(TeacherSchool_.school)
                    .get(School_.id));
        params.getSchoolIds().get().forEach(inPredicate::value);
        includeConjunctions.add(inPredicate);
      }

      // WHERE includeTeacher classXs.
      if (params.getClassXIds().isPresent() && !params.getClassXIds().isEmpty()) {
        var inPredicate =
            builder.in(
                DaoUtils.toJoin(
                        userX
                            .fetch(UserX_.teacher, JoinType.LEFT)
                            .fetch(Teacher_.teacherClassXES, JoinType.LEFT))
                    .get(TeacherClassX_.classX)
                    .get(ClassX_.id));
        params.getClassXIds().get().forEach(inPredicate::value);
        includeConjunctions.add(inPredicate);
      }

      whereDisjunctions.add(builder.and(includeConjunctions.toArray(new Predicate[0])));
    }

    // WHERE includeStudents.
    if (params.getIncludeStudents().orElse(false)) {
      List<Predicate> includeConjunctions = new ArrayList<>();
      includeConjunctions.add(builder.isNotNull(userX.get(UserX_.student)));

      // WHERE includeStudents schoolIds.
      if (params.getSchoolIds().isPresent() && !params.getSchoolIds().isEmpty()) {
        var inPredicate =
            builder.in(
                DaoUtils.toJoin(
                        userX
                            .fetch(UserX_.student, JoinType.LEFT)
                            .fetch(Student_.studentSchools, JoinType.LEFT))
                    .get(StudentSchool_.school)
                    .get(School_.id));
        params.getSchoolIds().get().forEach(inPredicate::value);
        includeConjunctions.add(inPredicate);
      }

      // WHERE includeStudent classXs.
      if (params.getClassXIds().isPresent() && !params.getClassXIds().isEmpty()) {
        var inPredicate =
            builder.in(
                DaoUtils.toJoin(
                        userX
                            .fetch(UserX_.student, JoinType.LEFT)
                            .fetch(Student_.studentClassXES, JoinType.LEFT))
                    .get(StudentClassX_.classX)
                    .get(ClassX_.id));
        params.getClassXIds().get().forEach(inPredicate::value);
        includeConjunctions.add(inPredicate);
      }

      whereDisjunctions.add(builder.and(includeConjunctions.toArray(new Predicate[0])));
    }

    // WHERE firstLastEmailSearchText.
    if (params.getFirstLastEmailSearchText().isPresent()) {
      var likePredicate =
          builder.like(
              builder.concat(
                  builder.concat(
                      builder.concat(userX.get(UserX_.firstName), " "),
                      builder.concat(userX.get(UserX_.lastName), ", ")),
                  builder.concat(
                      builder.concat(userX.get(UserX_.firstName), " "),
                      userX.get(UserX_.emailAddress))),
              "%" + params.getFirstLastEmailSearchText().get() + "%");
      whereConjunctions.add(likePredicate);
    }

    // WHERE - includeAdmins / includeStudents / includeTeachers.
    if (!whereDisjunctions.isEmpty()) {
      whereConjunctions.add(builder.or(whereDisjunctions.toArray(new Predicate[0])));
    } else {
      return new ArrayList<>();
    }

    // Register WHERE conjunctions.
    query.where(builder.and(whereConjunctions.toArray(new Predicate[0])));

    // SELECT.
    query.select(
        builder.tuple(Stream.of(userX).filter(Objects::nonNull).toArray(Selection<?>[]::new)));

    // Parse results.
    List<UserX> userXs = new ArrayList<>();
    for (Tuple row : entityManager.createQuery(query).getResultList()) {
      userXs.add(row.get(userX));
    }

    return userXs;
  }
}
