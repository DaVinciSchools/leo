package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.KnowledgeAndSkill.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface KnowledgeAndSkillRepository extends JpaRepository<KnowledgeAndSkill, Integer> {

  @Query("SELECT ks FROM KnowledgeAndSkill ks WHERE ks.type = (:type)")
  List<KnowledgeAndSkill> findAll(@Param("type") Type type);

  @Query("SELECT ks FROM KnowledgeAndSkill ks WHERE ks.id IN (:id) AND ks.type = (:type)")
  List<KnowledgeAndSkill> findAllByIdsAndType(
      @Param("id") Iterable<Integer> ids, @Param("type") Type type);

  @Query(
      """
          SELECT ks
          FROM KnowledgeAndSkill ks
          WHERE ks.type IN (:types)
          AND (ks.global OR ((:requiredUserXId) IS NULL OR ks.userX.id = (:requiredUserXId)))""")
  List<KnowledgeAndSkill> findAllByTypes(
      @Param("types") Iterable<Type> types, @Param("requiredUserXId") Integer userXId);

  @Query(
      "SELECT ks FROM KnowledgeAndSkill ks"
          + " INNER JOIN FETCH AssignmentKnowledgeAndSkill aks"
          + " ON aks.knowledgeAndSkill.id = ks.id"
          + " WHERE aks.assignment.id = (:assignmentId)")
  List<KnowledgeAndSkill> findAllByAssignmentId(@Param("assignmentId") int assignmentId);

  @Modifying
  @Transactional
  default Optional<KnowledgeAndSkill> guardedUpsert(
      KnowledgeAndSkill knowledgeAndSkill, Integer requiredUserXId) {
    checkNotNull(knowledgeAndSkill);

    if (knowledgeAndSkill.getId() == null || requiredUserXId == null) {
      return Optional.of(save(knowledgeAndSkill));
    }

    Optional<KnowledgeAndSkill> old = findById(knowledgeAndSkill.getId());
    if (old.isPresent() && Objects.equals(old.get().getUserX().getId(), requiredUserXId)) {
      return Optional.of(save(knowledgeAndSkill));
    }
    return old;
  }
}
