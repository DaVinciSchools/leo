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
public
class FindProjectsParams {

  @Nullable
  private Integer userXId;

  @Nullable private Integer projectId;

  @Nullable private Boolean includeInactive;

  @Nullable private Boolean includeUnsuccessful;

  @Nullable private Boolean includeTags;

  @Nullable private Boolean includeAssignment;

  @Nullable private Boolean includeMilestones;

  public Optional<Integer> getUserXId() {
    return Optional.ofNullable(userXId);
  }

  public Optional<Integer> getProjectId() {
    return Optional.ofNullable(projectId);
  }

  public Optional<Boolean> getIncludeInactive() {
    return Optional.ofNullable(includeInactive);
  }

  public Optional<Boolean> getIncludeUnsuccessful() {
    return Optional.ofNullable(includeUnsuccessful);
  }

  public Optional<Boolean> getIncludeTags() {
    return Optional.ofNullable(includeTags);
  }

  public Optional<Boolean> getIncludeAssignment() {
    return Optional.ofNullable(includeAssignment);
  }

  public Optional<Boolean> getIncludeMilestones() {
    return Optional.ofNullable(includeMilestones);
  }
}
