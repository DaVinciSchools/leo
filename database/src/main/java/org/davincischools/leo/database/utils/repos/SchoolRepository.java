package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends JpaRepository<School, Integer> {

  default School upsert(
      District district, @Nullable String nickname, String name, Consumer<School> modifier) {
    checkNotNull(district);
    checkArgument(nickname == null || !nickname.isEmpty());
    checkArgument(!Strings.isNullOrEmpty(name));
    checkNotNull(modifier);

    School school =
        findByName(district.getId(), name)
            .orElseGet(
                () ->
                    findByNickname(district.getId(), nickname)
                        .orElseGet(
                            () ->
                                new School().setCreationTime(Instant.now()).setDistrict(district)))
            .setNickname(nickname)
            .setName(name);

    modifier.accept(school);

    return saveAndFlush(school);
  }

  @Query(
      """
          SELECT s FROM School s
          INNER JOIN FETCH s.district
          WHERE s.district.id = (:districtId)
          """)
  List<School> findAllByDistrictId(@Param("districtId") int districtId);

  @Query(
      "SELECT s"
          + " FROM School s"
          + " INNER JOIN FETCH s.district"
          + " WHERE s.district.id = (:districtId)"
          + " AND s.name = (:name)")
  Optional<School> findByName(@Param("districtId") int districtId, @Param("name") String name);

  @Query(
      "SELECT s"
          + " FROM School s"
          + " INNER JOIN FETCH s.district"
          + " WHERE s.district.id = (:districtId)"
          + " AND s.nickname = (:nickname)")
  Optional<School> findByNickname(
      @Param("districtId") int districtId, @Param("nickname") String nickname);

  @Query(
      "SELECT s FROM School s"
          + " INNER JOIN FETCH TeacherSchool ts"
          + " ON ts.school.id = s.id"
          + " WHERE ts.teacher.id = (:teacherId)")
  List<School> findAllByTeacherId(@Param("teacherId") int teacherId);

  @Query(
      """
          SELECT s
          FROM School s
          LEFT JOIN FETCH s.district

          LEFT JOIN TeacherSchool ts
          ON
              (:teacher) IS NOT NULL
              AND s = ts.school
          LEFT JOIN StudentSchool ss
          ON
              (:student) IS NOT NULL
              AND s = ss.school

          WHERE (
              (:teacher) IS NULL
              OR ts.teacher = (:teacher))
          AND (
              (:student) IS NULL
              OR ss.student = (:student))
          AND (
              (:teacher) IS NOT NULL
              OR (:student) IS NOT NULL)
          """)
  List<School> findSchools(@Param("teacher") Teacher teacher, @Param("student") Student student);
}
