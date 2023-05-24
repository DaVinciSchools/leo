package org.davincischools.leo.database.utils.repos;

import com.google.common.collect.ImmutableList;
import java.util.EnumSet;
import java.util.Objects;
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
    roles.addAll(
        ImmutableList.of(
                user.getAdminX().getId() != null ? Role.ADMIN : null,
                user.getTeacher().getId() != null ? Role.TEACHER : null,
                user.getStudent().getId() != null ? Role.STUDENT : null)
            .stream()
            .filter(Objects::nonNull)
            .toList());
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
