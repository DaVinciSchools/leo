package org.davincischools.leo.database.utils.repos;

import java.time.Instant;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.ClassXKnowledgeAndSkill;
import org.davincischools.leo.database.daos.ClassXKnowledgeAndSkillId;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassXKnowledgeAndSkillRepository
    extends JpaRepository<ClassXKnowledgeAndSkill, ClassXKnowledgeAndSkillId> {

  default ClassXKnowledgeAndSkill saveClassXKnowledgeAndSkill(
      ClassX classX, KnowledgeAndSkill knowledgeAndSkill) {
    return saveAndFlush(
        new ClassXKnowledgeAndSkill()
            .setCreationTime(Instant.now())
            .setId(
                new ClassXKnowledgeAndSkillId()
                    .setClassXId(classX.getId())
                    .setKnowledgeAndSkillId(knowledgeAndSkill.getId()))
            .setClassX(classX)
            .setKnowledgeAndSkill(knowledgeAndSkill));
  }
}
