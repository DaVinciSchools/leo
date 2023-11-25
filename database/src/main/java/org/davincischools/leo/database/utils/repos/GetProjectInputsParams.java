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
public class GetProjectInputsParams {

  @Nullable private GetAssignmentsParams includeAssignment;
  // One of the following fields must be set to get any results.
  @Nullable private Boolean includeComplete;
  @Nullable private Boolean includeProcessing;

  @Nullable private Iterable<Integer> userXIds;
  @Nullable private Iterable<Integer> projectInputIds;

  public Optional<Iterable<Integer>> getUserXIds() {
    return Optional.ofNullable(userXIds);
  }

  public Optional<Iterable<Integer>> getProjectInputIds() {
    return Optional.ofNullable(projectInputIds);
  }

  public Optional<Boolean> getIncludeComplete() {
    return Optional.ofNullable(includeComplete);
  }

  public Optional<Boolean> getIncludeProcessing() {
    return Optional.ofNullable(includeProcessing);
  }

  public Optional<GetAssignmentsParams> getIncludeAssignment() {
    return Optional.ofNullable(includeAssignment);
  }
}
