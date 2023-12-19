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
public class GetUserXsParams {

  @Nullable private Boolean includeSchools;
  @Nullable private GetClassXsParams includeClassXs;

  @Nullable private Iterable<Integer> inDistrictIds;
  @Nullable private Iterable<Integer> inUserXIds;
  @Nullable private Iterable<Integer> inSchoolIds;
  @Nullable private Iterable<Integer> inClassXIds;
  @Nullable private Iterable<Integer> excludeUserXIds;

  @Nullable private String hasEmailAddress;

  @Nullable private Boolean adminXsOnly;
  @Nullable private Boolean teachersOnly;
  @Nullable private Boolean studentsOnly;

  @Nullable private String firstLastEmailSearchText;

  @Nullable private Integer page;
  @Nullable private Integer pageSize;

  Optional<Boolean> getIncludeSchools() {
    return Optional.ofNullable(includeSchools);
  }

  Optional<GetClassXsParams> getIncludeClassXs() {
    return Optional.ofNullable(includeClassXs);
  }

  Optional<Boolean> getAdminXsOnly() {
    return Optional.ofNullable(adminXsOnly);
  }

  Optional<Boolean> getTeachersOnly() {
    return Optional.ofNullable(teachersOnly);
  }

  Optional<Boolean> getStudentsOnly() {
    return Optional.ofNullable(studentsOnly);
  }

  Optional<String> getHasEmailAddress() {
    return Optional.ofNullable(hasEmailAddress);
  }

  Optional<Iterable<Integer>> getInDistrictIds() {
    return Optional.ofNullable(inDistrictIds);
  }

  Optional<Iterable<Integer>> getInUserXIds() {
    return Optional.ofNullable(inUserXIds);
  }

  Optional<Iterable<Integer>> getInSchoolIds() {
    return Optional.ofNullable(inSchoolIds);
  }

  Optional<Iterable<Integer>> getInClassXIds() {
    return Optional.ofNullable(inClassXIds);
  }

  Optional<Iterable<Integer>> getExcludeUserXIds() {
    return Optional.ofNullable(excludeUserXIds);
  }

  Optional<String> getFirstLastEmailSearchText() {
    return Optional.ofNullable(firstLastEmailSearchText);
  }

  Optional<Integer> getPage() {
    return Optional.ofNullable(page);
  }

  Optional<Integer> getPageSize() {
    return Optional.ofNullable(pageSize);
  }
}
