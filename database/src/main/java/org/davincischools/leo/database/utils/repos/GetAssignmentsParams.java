package org.davincischools.leo.database.utils.repos;

import java.util.List;
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

  @Nullable private List<Integer> assignmentIds;
  @Nullable private List<Integer> classXIds;
  @Nullable private List<Integer> studentIds;
  @Nullable private List<Integer> teacherIds;

  public Optional<Boolean> getIncludeClassXs() {
    return Optional.ofNullable(includeClassXs);
  }

  public Optional<Boolean> getIncludeKnowledgeAndSkills() {
    return Optional.ofNullable(includeKnowledgeAndSkills);
  }

  public Optional<List<Integer>> getAssignmentIds() {
    return Optional.ofNullable(assignmentIds);
  }

  public Optional<List<Integer>> getClassXIds() {
    return Optional.ofNullable(classXIds);
  }

  public Optional<List<Integer>> getStudentIds() {
    return Optional.ofNullable(studentIds);
  }

  public Optional<List<Integer>> getTeacherIds() {
    return Optional.ofNullable(teacherIds);
  }
}
