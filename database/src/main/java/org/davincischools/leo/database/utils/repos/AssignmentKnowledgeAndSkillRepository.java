package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

import com.google.common.collect.Streams;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.AssignmentKnowledgeAndSkill;
import org.davincischools.leo.database.daos.AssignmentKnowledgeAndSkillId;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentKnowledgeAndSkillRepository
    extends JpaRepository<AssignmentKnowledgeAndSkill, AssignmentKnowledgeAndSkillId> {

  static AssignmentKnowledgeAndSkillId createId(
      Assignment assignment, KnowledgeAndSkill knowledgeAndSkill) {
    checkNotNull(assignment);
    checkNotNull(knowledgeAndSkill);

    return new AssignmentKnowledgeAndSkillId()
        .setAssignmentId(assignment.getId())
        .setKnowledgeAndSkillId(knowledgeAndSkill.getId());
  }

  static AssignmentKnowledgeAndSkill create(
      Assignment assignment, KnowledgeAndSkill knowledgeAndSkill) {
    checkNotNull(assignment);
    checkNotNull(knowledgeAndSkill);

    return new AssignmentKnowledgeAndSkill()
        .setCreationTime(Instant.now())
        .setId(createId(assignment, knowledgeAndSkill))
        .setAssignment(assignment)
        .setKnowledgeAndSkill(knowledgeAndSkill);
  }

  @Query(
      """
          SELECT aks
          FROM AssignmentKnowledgeAndSkill aks
          WHERE aks.assignment.id = (:assignment_id)
          """)
  List<AssignmentKnowledgeAndSkill> selectAllByAssignmentId(
      @Param("assignment_id") int assignmentId);

  default AssignmentKnowledgeAndSkill upsert(
      Assignment assignment, KnowledgeAndSkill knowledgeAndSkill) {
    checkNotNull(assignment);
    checkNotNull(knowledgeAndSkill);

    return saveAndFlush(
        new AssignmentKnowledgeAndSkill()
            .setCreationTime(Instant.now())
            .setId(
                new AssignmentKnowledgeAndSkillId()
                    .setAssignmentId(assignment.getId())
                    .setKnowledgeAndSkillId(knowledgeAndSkill.getId()))
            .setAssignment(assignment)
            .setKnowledgeAndSkill(knowledgeAndSkill));
  }

  @Transactional
  default void setAssignmentKnowledgeAndSkills(
      Assignment assignment, Iterable<KnowledgeAndSkill> knowledgeAndSkills) {
    List<AssignmentKnowledgeAndSkill> current = selectAllByAssignmentId(assignment.getId());
    Set<Integer> currentKnowledgeAndSkillIds =
        current.stream().map(e -> e.getKnowledgeAndSkill().getId()).collect(toSet());

    List<AssignmentKnowledgeAndSkill> newKnowledgeAndSkills =
        Streams.stream(knowledgeAndSkills)
            .filter(e -> !currentKnowledgeAndSkillIds.contains(e.getId()))
            .map(e -> create(assignment, e))
            .toList();
    saveAll(newKnowledgeAndSkills);

    Set<Integer> knowledgeAndSkillIds =
        Streams.stream(knowledgeAndSkills).map(KnowledgeAndSkill::getId).collect(toSet());
    List<AssignmentKnowledgeAndSkill> toDelete =
        current.stream()
            .filter(e -> !knowledgeAndSkillIds.contains(e.getKnowledgeAndSkill().getId()))
            .toList();
    deleteAll(toDelete);
  }
}
