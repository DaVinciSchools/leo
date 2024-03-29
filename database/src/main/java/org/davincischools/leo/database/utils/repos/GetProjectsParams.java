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
public class GetProjectsParams {

  @Nullable private Boolean includeInactive;
  @Nullable private Boolean includeTags;
  @Nullable private GetProjectInputsParams includeInputs;
  @Nullable private Boolean includeFulfillments;
  @Nullable private GetAssignmentsParams includeAssignment;
  @Nullable private Boolean includeMilestones;
  @Nullable private GetProjectPostsParams includeProjectPosts;

  @Nullable private Iterable<Integer> userXIds;
  @Nullable private Iterable<Integer> projectIds;

  public Optional<Iterable<Integer>> getUserXIds() {
    return Optional.ofNullable(userXIds);
  }

  public Optional<Iterable<Integer>> getProjectIds() {
    return Optional.ofNullable(projectIds);
  }

  public Optional<Boolean> getIncludeInactive() {
    return Optional.ofNullable(includeInactive);
  }

  public Optional<Boolean> getIncludeTags() {
    return Optional.ofNullable(includeTags);
  }

  public Optional<GetProjectInputsParams> getIncludeInputs() {
    return Optional.ofNullable(includeInputs);
  }

  public Optional<Boolean> getIncludeFulfillments() {
    return Optional.ofNullable(includeFulfillments);
  }

  public Optional<GetAssignmentsParams> getIncludeAssignment() {
    return Optional.ofNullable(includeAssignment);
  }

  public Optional<Boolean> getIncludeMilestones() {
    return Optional.ofNullable(includeMilestones);
  }

  public Optional<GetProjectPostsParams> getIncludeProjectPosts() {
    return Optional.ofNullable(includeProjectPosts);
  }
}
