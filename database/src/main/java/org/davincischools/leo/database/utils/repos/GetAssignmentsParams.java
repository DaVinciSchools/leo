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
public class GetAssignmentsParams {

  @Nullable private Boolean includeClassXs;
  @Nullable private Boolean includeKnowledgeAndSkills;

  @Nullable private Iterable<Integer> assignmentIds;
  @Nullable private Iterable<Integer> classXIds;
  @Nullable private Iterable<Integer> studentIds;
  @Nullable private Iterable<Integer> teacherIds;

  public Optional<Boolean> getIncludeClassXs() {
    return Optional.ofNullable(includeClassXs);
  }

  public Optional<Boolean> getIncludeKnowledgeAndSkills() {
    return Optional.ofNullable(includeKnowledgeAndSkills);
  }

  public Optional<Iterable<Integer>> getAssignmentIds() {
    return Optional.ofNullable(assignmentIds);
  }

  public Optional<Iterable<Integer>> getClassXIds() {
    return Optional.ofNullable(classXIds);
  }

  public Optional<Iterable<Integer>> getStudentIds() {
    return Optional.ofNullable(studentIds);
  }

  public Optional<Iterable<Integer>> getTeacherIds() {
    return Optional.ofNullable(teacherIds);
  }
}
