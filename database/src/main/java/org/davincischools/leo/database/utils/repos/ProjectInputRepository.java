package org.davincischools.leo.database.utils.repos;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.davincischools.leo.database.daos.ProjectDefinition;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory;
import org.davincischools.leo.database.daos.ProjectDefinitionCategoryType;
import org.davincischools.leo.database.daos.ProjectInput;
import org.davincischools.leo.database.daos.ProjectInputValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProjectInputRepository extends JpaRepository<ProjectInput, Integer> {

  enum State {
    PROCESSING,
    COMPLETED,
    FAILED
  }

  record InputCategoryValues(
      ProjectDefinitionCategory category,
      ProjectDefinitionCategoryType type,
      List<ProjectInputValue> values) {}

  record FullProjectInput(
      ProjectInput input, ProjectDefinition definition, List<InputCategoryValues> values) {}

  record FullProjectInputRow(
      ProjectInput input,
      ProjectDefinition definition,
      ProjectDefinitionCategory category,
      ProjectDefinitionCategoryType type,
      ProjectInputValue value) {}

  @Query(
      """
      SELECT i, d, c, t, v
      FROM ProjectInput i
      LEFT JOIN FETCH i.projectDefinition d
      LEFT JOIN ProjectInputValue v
      ON i.id = v.projectInput.id
      LEFT JOIN FETCH v.knowledgeAndSkillValue
      LEFT JOIN FETCH v.motivationValue
      LEFT JOIN FETCH v.projectDefinitionCategory c
      LEFT JOIN FETCH c.projectDefinitionCategoryType t
      WHERE ((:projectInputId) IS NULL OR i.id = (:projectInputId))
      AND ((:projectNotSuccessful) IS NULL OR i.state <> 'SUCCESS')
      AND ((:userXId) IS NULL OR d.userX.id = (:userXId))
      ORDER BY i.id DESC, c.position, v.position""")
  List<FullProjectInputRow> findFullProjectInputRowsById(
      @Param("projectInputId") Integer projectInputId,
      @Param("userXId") Integer userXId,
      // NULL or true only.
      @Param("projectNotSuccessful") Boolean projectNotSuccessful);

  default Optional<FullProjectInput> findFullProjectInputById(int projectInputId) {
    return Optional.ofNullable(
        Iterables.getOnlyElement(
            toFullProjectInputs(findFullProjectInputRowsById(projectInputId, null, null)), null));
  }

  default List<FullProjectInput> findFullProjectInputByUserXAndUnsuccessful(int userXId) {
    return toFullProjectInputs(findFullProjectInputRowsById(null, userXId, true));
  }

  private List<FullProjectInput> toFullProjectInputs(Iterable<FullProjectInputRow> rows) {
    List<FullProjectInput> allInputs = new ArrayList<>();
    FullProjectInput input = null;
    InputCategoryValues values = null;

    for (FullProjectInputRow row : rows) {
      if (input == null || !Objects.equals(input.input().getId(), row.input().getId())) {
        allInputs.add(
            input = new FullProjectInput(row.input(), row.definition(), new ArrayList<>()));
        values = null;
      }
      if (row.category() == null || row.type() == null) {
        continue;
      }
      if (values == null || !Objects.equals(values.category().getId(), row.category().getId())) {
        input
            .values()
            .add(values = new InputCategoryValues(row.category(), row.type(), new ArrayList<>()));
      }
      if (row.value() != null) {
        values.values().add(row.value());
      }
    }

    return allInputs;
  }

  @Modifying
  @Transactional
  @Query("UPDATE ProjectInput p SET p.state = (:state) WHERE p.id = (:id)")
  void updateState(@Param("id") int id, @Param("state") String state);

  @Modifying
  @Transactional
  @Query(
      "UPDATE ProjectInput p SET p.userX.id = (:userXId) WHERE p.id = (:id) AND p.userX.id IS NULL")
  void updateUserX(@Param("id") int id, @Param("userXId") int userXId);
}
