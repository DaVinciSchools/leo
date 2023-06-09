package org.davincischools.leo.database.utils.repos;

import java.util.Optional;
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

  @Query("SELECT ks FROM KnowledgeAndSkill ks WHERE ks.type = (:type)")
  Iterable<KnowledgeAndSkill> findAll(@Param("type") String type);

  @Query("SELECT ks FROM KnowledgeAndSkill ks WHERE ks.id IN (:id) AND ks.type = (:type)")
  Iterable<KnowledgeAndSkill> findAllByIdsAndType(
      @Param("id") Iterable<Integer> ids, @Param("type") String type);

  @Query(
      "SELECT ks FROM KnowledgeAndSkill ks"
          + " INNER JOIN FETCH AssignmentKnowledgeAndSkill aks"
          + " ON aks.knowledgeAndSkill.id = ks.id"
          + " WHERE aks.assignment.id = (:assignmentId)")
  Iterable<KnowledgeAndSkill> findAllByAssignmentId(@Param("assignmentId") int assignmentId);

  @Query(
      "SELECT ks"
          + " FROM KnowledgeAndSkill ks"
          + " WHERE ks.classX.id = (:classId)"
          + " AND ks.name = (:name)")
  Optional<KnowledgeAndSkill> findByName(@Param("classId") int classId, @Param("name") String name);
}
