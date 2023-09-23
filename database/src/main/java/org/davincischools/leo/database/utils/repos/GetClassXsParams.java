package org.davincischools.leo.database.utils.repos;

import java.util.Optional;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor
public class GetClassXsParams {

  @Nullable private Boolean includeSchool;
  @Nullable private Boolean includeAssignments;
  @Nullable private Boolean includeKnowledgeAndSkills;

  @Nullable private Iterable<Integer> schoolIds;
  @Nullable private Iterable<Integer> classXIds;
  @Nullable private Iterable<Integer> teacherIds;
  @Nullable private Iterable<Integer> studentIds;

  Optional<Boolean> getIncludeSchool() {
    return Optional.ofNullable(includeSchool);
  }

  Optional<Boolean> getIncludeAssignments() {
    return Optional.ofNullable(includeAssignments);
  }

  Optional<Boolean> getIncludeKnowledgeAndSkills() {
    return Optional.ofNullable(includeKnowledgeAndSkills);
  }

  Optional<Iterable<Integer>> getSchoolIds() {
    return Optional.ofNullable(schoolIds);
  }

  Optional<Iterable<Integer>> getClassXIds() {
    return Optional.ofNullable(classXIds);
  }

  Optional<Iterable<Integer>> getTeacherIds() {
    return Optional.ofNullable(teacherIds);
  }

  Optional<Iterable<Integer>> getStudentIds() {
    return Optional.ofNullable(studentIds);
  }
}
