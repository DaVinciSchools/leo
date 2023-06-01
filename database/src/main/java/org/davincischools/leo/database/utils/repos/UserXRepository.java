package org.davincischools.leo.database.utils.repos;

import java.util.EnumSet;
import java.util.Optional;
import org.davincischools.leo.database.daos.UserX;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserXRepository extends JpaRepository<UserX, Integer> {

  enum Role {
    ADMIN,
    TEACHER,
    STUDENT
  }

  public static EnumSet<Role> getRoles(UserX user) {
    EnumSet<Role> roles = EnumSet.noneOf(Role.class);
    if (user.getAdminX() != null && user.getAdminX().getId() != null) {
      roles.add(Role.ADMIN);
    }
    if (user.getTeacher() != null && user.getTeacher().getId() != null) {
      roles.add(Role.TEACHER);
    }
    if (user.getStudent() != null && user.getStudent().getId() != null) {
      roles.add(Role.STUDENT);
    }
    return roles;
  }

  @Query(
      "SELECT u FROM UserX u"
          + " LEFT JOIN FETCH u.adminX"
          + " LEFT JOIN FETCH u.teacher"
          + " LEFT JOIN FETCH u.student"
          + " WHERE u.id = (:userXId)")
  Optional<UserX> findById(@Param("userXId") int userXId);

  @Query(
      "SELECT u FROM UserX u"
          + " LEFT JOIN FETCH u.adminX"
          + " LEFT JOIN FETCH u.teacher"
          + " LEFT JOIN FETCH u.student"
          + " WHERE u.teacher.id = (:teacherId)")
  Optional<UserX> findByTeacherId(@Param("teacherId") int teacherId);

  @Query(
      "SELECT u FROM UserX u"
          + " LEFT JOIN FETCH u.adminX"
          + " LEFT JOIN FETCH u.teacher"
          + " LEFT JOIN FETCH u.student"
          + " WHERE u.emailAddress = (:emailAddress)")
  Optional<UserX> findByEmailAddress(@Param("emailAddress") String emailAddress);

  @Query(
      "SELECT u FROM UserX u"
          + " LEFT JOIN FETCH u.adminX"
          + " LEFT JOIN FETCH u.teacher"
          + " LEFT JOIN FETCH u.student"
          + " WHERE u.district.id = (:districtId)")
  Iterable<UserX> findAllByDistrictId(@Param("districtId") int districtId);
}
