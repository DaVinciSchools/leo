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
public class GetProjectDefinitionsParams {

  @Nullable private Iterable<Integer> projectDefinitionIds;
  @Nullable private Iterable<Integer> assignmentIds;

  public Optional<Iterable<Integer>> getProjectDefinitionIds() {
    return Optional.ofNullable(projectDefinitionIds);
  }

  public Optional<Iterable<Integer>> getAssignmentIds() {
    return Optional.ofNullable(assignmentIds);
  }
}
