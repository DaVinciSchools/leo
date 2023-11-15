package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import org.davincischools.leo.database.daos.ClassX_;
import org.davincischools.leo.database.daos.District_;
import org.davincischools.leo.database.daos.School_;
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
import org.davincischools.leo.database.utils.QueryHelper.QueryHelperUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserXRepository extends JpaRepository<UserX, Integer>, AutowiredRepositoryValues {

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
            (u, projectPost, builder) -> configureQuery(u, projectPost, builder, params),
            PageRequest.of(
                params.getPage().orElse(0), params.getPageSize().orElse(Integer.MAX_VALUE)));
  }

  public static void configureQuery(
      QueryHelperUtils u, From<?, UserX> userX, CriteriaBuilder builder, GetUserXsParams params) {
    checkNotNull(u);
    checkNotNull(userX);
    checkNotNull(builder);
    checkNotNull(params);

    u.notDeleted(userX);

    var district = u.notDeleted(u.fetch(userX, UserX_.district, JoinType.LEFT));
    var adminX = u.notDeleted(u.fetch(userX, UserX_.adminX, JoinType.LEFT));
    var teacher = u.notDeleted(u.fetch(userX, UserX_.teacher, JoinType.LEFT));
    var student = u.notDeleted(u.fetch(userX, UserX_.student, JoinType.LEFT));

    if (params.getInDistrictIds().isPresent()) {
      u.where(district.get(District_.id).in(ImmutableList.copyOf(params.getInDistrictIds().get())));
    }

    if (params.getInUserXIds().isPresent()) {
      u.where(userX.get(UserX_.id).in(ImmutableList.copyOf(params.getInUserXIds().get())));
    }

    if (params.getIncludeSchools().orElse(false) || params.getInSchoolIds().isPresent()) {
      var teacherSchools =
          u.notDeleted(
              u.fetch(
                  teacher,
                  Teacher_.teacherSchools,
                  JoinType.LEFT,
                  TeacherSchool::getTeacher,
                  Teacher::setTeacherSchools));
      var teacherSchool =
          u.notDeleted(u.fetch(teacherSchools, TeacherSchool_.school, JoinType.LEFT));

      var studentSchools =
          u.notDeleted(
              u.fetch(
                  student,
                  Student_.studentSchools,
                  JoinType.LEFT,
                  StudentSchool::getStudent,
                  Student::setStudentSchools));
      var studentSchool =
          u.notDeleted(u.fetch(studentSchools, StudentSchool_.school, JoinType.LEFT));

      if (params.getInSchoolIds().isPresent()) {
        u.where(
            builder.or(
                teacherSchool
                    .get(School_.id)
                    .in(ImmutableList.copyOf(params.getInSchoolIds().get())),
                studentSchool
                    .get(School_.id)
                    .in(ImmutableList.copyOf(params.getInSchoolIds().get()))));
      }
    }

    if (params.getIncludeClassXs().orElse(false) || params.getInClassXIds().isPresent()) {
      var teacherClassXs =
          u.notDeleted(
              u.fetch(
                  teacher,
                  Teacher_.teacherClassXES,
                  JoinType.LEFT,
                  TeacherClassX::getTeacher,
                  Teacher::setTeacherClassXES));
      var teacherClassX =
          u.notDeleted(u.fetch(teacherClassXs, TeacherClassX_.classX, JoinType.LEFT));

      var studentClassXs =
          u.notDeleted(
              u.fetch(
                  student,
                  Student_.studentClassXES,
                  JoinType.LEFT,
                  StudentClassX::getStudent,
                  Student::setStudentClassXES));
      var studentClassX =
          u.notDeleted(u.fetch(studentClassXs, StudentClassX_.classX, JoinType.LEFT));

      if (params.getInSchoolIds().isPresent()) {
        u.where(
            builder.or(
                teacherClassX
                    .get(ClassX_.id)
                    .in(ImmutableList.copyOf(params.getInClassXIds().get())),
                studentClassX
                    .get(ClassX_.id)
                    .in(ImmutableList.copyOf(params.getInClassXIds().get()))));
      }
    }

    if (params.getHasEmailAddress().isPresent()) {
      u.where(builder.equal(userX.get(UserX_.emailAddress), params.getHasEmailAddress().get()));
    }

    if (params.getAdminXsOnly().orElse(false)) {
      u.where(adminX.isNotNull());
    }

    if (params.getTeachersOnly().orElse(false)) {
      u.where(teacher.isNotNull());
    }

    if (params.getStudentsOnly().orElse(false)) {
      u.where(student.isNotNull());
    }

    if (params.getFirstLastEmailSearchText().isPresent()) {
      u.where(
          builder.like(
              builder.concat(
                  builder.concat(
                      builder.concat(userX.get(UserX_.firstName), " "),
                      builder.concat(userX.get(UserX_.lastName), ", ")),
                  builder.concat(
                      builder.concat(userX.get(UserX_.firstName), " "),
                      userX.get(UserX_.emailAddress))),
              "%" + params.getFirstLastEmailSearchText().get() + "%"));
    }

    u.orderBy(
        builder.asc(userX.get(UserX_.lastName)),
        builder.asc(userX.get(UserX_.firstName)),
        builder.asc(userX.get(UserX_.emailAddress)));
  }
}
