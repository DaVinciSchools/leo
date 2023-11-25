package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.davincischools.leo.database.utils.query_helper.QueryHelper.DEFAULT_PAGE_SIZE;

import jakarta.persistence.criteria.JoinType;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.StudentClassX;
import org.davincischools.leo.database.daos.StudentClassX_;
import org.davincischools.leo.database.daos.StudentSchool;
import org.davincischools.leo.database.daos.StudentSchool_;
import org.davincischools.leo.database.daos.Student_;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.TeacherClassX;
import org.davincischools.leo.database.daos.TeacherClassX_;
import org.davincischools.leo.database.daos.TeacherSchool;
import org.davincischools.leo.database.daos.TeacherSchool_;
import org.davincischools.leo.database.daos.Teacher_;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.daos.UserX_;
import org.davincischools.leo.database.utils.EntityUtils;
import org.davincischools.leo.database.utils.query_helper.Entity;
import org.davincischools.leo.database.utils.query_helper.Expression;
import org.davincischools.leo.database.utils.query_helper.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserXRepository extends JpaRepository<UserX, Integer>, AutowiredRepositoryValues {

  int MAX_EMAIL_ADDRESS_LENGTH =
      EntityUtils.getColumn(UserX.class, UserX.COLUMN_EMAILADDRESS_NAME).length();
  int MAX_FIRST_NAME_LENGTH =
      EntityUtils.getColumn(UserX.class, UserX.COLUMN_FIRSTNAME_NAME).length();
  int MAX_LAST_NAME_LENGTH =
      EntityUtils.getColumn(UserX.class, UserX.COLUMN_LASTNAME_NAME).length();

  public static final int MIN_PASSWORD_LENGTH = 8;
  String INVALID_ENCODED_PASSWORD = "INVALID ENCODED PASSWORD";

  enum Role {
    ADMIN_X,
    TEACHER,
    STUDENT,
    DEMO
  }

  String PROJECT_LEO_COACH_EMAIL = "coach_leo@projectleo.net";

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

  default UserX getProjectLeoCoach() {
    return getProjectLeoCoach(this);
  }

  default Optional<UserX> findById(int userXId) {
    return getUserXs(new GetUserXsParams().setInUserXIds(List.of(userXId))).stream().findFirst();
  }

  default Optional<UserX> findByEmailAddress(String emailAddress) {
    return getUserXs(new GetUserXsParams().setHasEmailAddress(emailAddress)).stream().findFirst();
  }

  default Page<UserX> getUserXs(GetUserXsParams params) {
    checkNotNull(params);

    return getQueryHelper()
        .query(
            UserX.class,
            userX -> configureQuery(userX, params),
            params
                .getPage()
                .map(
                    page ->
                        Pageable.ofSize(params.getPageSize().orElse(DEFAULT_PAGE_SIZE))
                            .withPage(page))
                .orElse(Pageable.unpaged()));
  }

  public static void configureQuery(Entity<?, UserX> userX, GetUserXsParams params) {
    checkNotNull(userX);
    checkNotNull(params);

    userX.notDeleted().fetch().requireId(params.getInUserXIds());

    // var district =
    userX
        .join(UserX_.district, JoinType.LEFT)
        .notDeleted()
        .fetch()
        .requireId(params.getInDistrictIds());
    var adminX = userX.join(UserX_.adminX, JoinType.LEFT).notDeleted().fetch();
    var teacher = userX.join(UserX_.teacher, JoinType.LEFT).notDeleted().fetch();
    var student = userX.join(UserX_.student, JoinType.LEFT).notDeleted().fetch();

    var teacherSchool =
        teacher.supplier(
            () ->
                teacher
                    .join(
                        Teacher_.teacherSchools,
                        JoinType.LEFT,
                        TeacherSchool::getTeacher,
                        Teacher::setTeacherSchools)
                    .notDeleted()
                    .join(TeacherSchool_.school, JoinType.LEFT)
                    .notDeleted(),
            params.getInSchoolIds());
    var studentSchool =
        student.supplier(
            () ->
                student
                    .join(
                        Student_.studentSchools,
                        JoinType.LEFT,
                        StudentSchool::getStudent,
                        Student::setStudentSchools)
                    .notDeleted()
                    .join(StudentSchool_.school, JoinType.LEFT)
                    .notDeleted(),
            params.getInSchoolIds());
    if (params.getIncludeSchools().orElse(false)) {
      teacherSchool.get().fetch();
      studentSchool.get().fetch();
    }

    var teacherClassX =
        teacher.supplier(
            () ->
                teacher
                    .join(
                        Teacher_.teacherClassXES,
                        JoinType.LEFT,
                        TeacherClassX::getTeacher,
                        Teacher::setTeacherClassXES)
                    .notDeleted()
                    .join(TeacherClassX_.classX, JoinType.LEFT)
                    .notDeleted(),
            params.getInClassXIds());
    var studentClassX =
        student.supplier(
            () ->
                student
                    .join(
                        Student_.studentClassXES,
                        JoinType.LEFT,
                        StudentClassX::getStudent,
                        Student::setStudentClassXES)
                    .notDeleted()
                    .join(StudentClassX_.classX, JoinType.LEFT)
                    .notDeleted(),
            params.getInClassXIds());
    if (params.getIncludeClassXs().isPresent()) {
      ClassXRepository.configureQuery(teacherClassX.get(), params.getIncludeClassXs().get());
      ClassXRepository.configureQuery(studentClassX.get(), params.getIncludeClassXs().get());
    }

    if (params.getHasEmailAddress().isPresent()) {
      userX.where(Predicate.eq(userX.get(UserX_.emailAddress), params.getHasEmailAddress().get()));
    }

    if (params.getAdminXsOnly().orElse(false)) {
      userX.where(Predicate.isNotNull(adminX));
    }

    if (params.getTeachersOnly().orElse(false)) {
      userX.where(Predicate.isNotNull(teacher));
    }

    if (params.getStudentsOnly().orElse(false)) {
      userX.where(Predicate.isNotNull(student));
    }

    if (params.getFirstLastEmailSearchText().isPresent()) {
      userX.where(
          Predicate.like(
              Expression.concat(
                  userX.get(UserX_.firstName),
                  Expression.literal(" "),
                  userX.get(UserX_.lastName),
                  Expression.literal(", "),
                  userX.get(UserX_.firstName),
                  Expression.literal(" "),
                  userX.get(UserX_.emailAddress)),
              "%" + params.getFirstLastEmailSearchText().get() + "%"));
    }

    userX.orderByAsc(userX.get(UserX_.lastName));
    userX.orderByAsc(userX.get(UserX_.firstName));
    userX.orderByAsc(userX.get(UserX_.emailAddress));
  }
}
