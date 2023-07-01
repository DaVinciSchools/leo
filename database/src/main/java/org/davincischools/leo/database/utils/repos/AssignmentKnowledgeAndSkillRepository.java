package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.Instant;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.AssignmentKnowledgeAndSkill;
import org.davincischools.leo.database.daos.AssignmentKnowledgeAndSkillId;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentKnowledgeAndSkillRepository
    extends JpaRepository<AssignmentKnowledgeAndSkill, AssignmentKnowledgeAndSkillId> {

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
}
