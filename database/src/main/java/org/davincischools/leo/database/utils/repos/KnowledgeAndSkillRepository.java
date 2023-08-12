package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface KnowledgeAndSkillRepository extends JpaRepository<KnowledgeAndSkill, Integer> {

  enum Type {
    EKS,
    XQ_COMPETENCY,
  }

  default KnowledgeAndSkill upsert(String name, Type type, Consumer<KnowledgeAndSkill> modifier) {
    checkArgument(!Strings.isNullOrEmpty(name));
    checkNotNull(type);
    checkNotNull(modifier);

    KnowledgeAndSkill knowledgeAndSkill =
        findByNameAndType(name, type.name())
            .orElseGet(() -> new KnowledgeAndSkill().setCreationTime(Instant.now()))
            .setName(name)
            .setType(type.name());

    modifier.accept(knowledgeAndSkill);

    return saveAndFlush(knowledgeAndSkill);
  }

  @Query("SELECT ks FROM KnowledgeAndSkill ks WHERE ks.type = (:type)")
  List<KnowledgeAndSkill> findAll(@Param("type") String type);

  @Query("SELECT ks FROM KnowledgeAndSkill ks WHERE ks.id IN (:id) AND ks.type = (:type)")
  List<KnowledgeAndSkill> findAllByIdsAndType(
      @Param("id") Iterable<Integer> ids, @Param("type") String type);

  @Query(
      """
          SELECT ks
          FROM KnowledgeAndSkill ks
          WHERE ks.type IN (:types)
          AND (ks.global OR ks.userX.id = (:user_x_id))""")
  List<KnowledgeAndSkill> findAllByTypes(
      @Param("types") Iterable<String> types, @Param("user_x_id") int userXId);

  @Query(
      "SELECT ks FROM KnowledgeAndSkill ks"
          + " INNER JOIN FETCH AssignmentKnowledgeAndSkill aks"
          + " ON aks.knowledgeAndSkill.id = ks.id"
          + " WHERE aks.assignment.id = (:assignmentId)")
  List<KnowledgeAndSkill> findAllByAssignmentId(@Param("assignmentId") int assignmentId);

  @Query(
      "SELECT ks"
          + " FROM KnowledgeAndSkill ks"
          + " WHERE ks.name = (:name)"
          + " AND ks.type = (:type)")
  Optional<KnowledgeAndSkill> findByNameAndType(
      @Param("name") String name, @Param("type") String type);
}
