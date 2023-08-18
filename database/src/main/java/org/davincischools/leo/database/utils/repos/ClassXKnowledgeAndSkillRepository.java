package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.ClassXKnowledgeAndSkill;
import org.davincischools.leo.database.daos.ClassXKnowledgeAndSkillId;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassXKnowledgeAndSkillRepository
    extends JpaRepository<ClassXKnowledgeAndSkill, ClassXKnowledgeAndSkillId> {

  default ClassXKnowledgeAndSkillId createId(ClassX classX, KnowledgeAndSkill knowledgeAndSkill) {
    checkNotNull(classX);
    checkNotNull(knowledgeAndSkill);

    return new ClassXKnowledgeAndSkillId()
        .setClassXId(classX.getId())
        .setKnowledgeAndSkillId(knowledgeAndSkill.getId());
  }

  default ClassXKnowledgeAndSkill create(ClassX classX, KnowledgeAndSkill knowledgeAndSkill) {
    checkNotNull(classX);
    checkNotNull(knowledgeAndSkill);

    return new ClassXKnowledgeAndSkill()
        .setCreationTime(Instant.now())
        .setId(createId(classX, knowledgeAndSkill))
        .setClassX(classX)
        .setKnowledgeAndSkill(knowledgeAndSkill);
  }

  default ClassXKnowledgeAndSkill upsert(ClassX classX, KnowledgeAndSkill knowledgeAndSkill) {
    checkNotNull(classX);
    checkNotNull(knowledgeAndSkill);

    return saveAndFlush(create(classX, knowledgeAndSkill));
  }

  // TODO: This doesn't work for some reason. It's not sending a delete query to the database.
  // @Modifying
  // @Transactional
  // @Query(
  // """
  // DELETE FROM ClassXKnowledgeAndSkill cxks
  // WHERE cxks.classX.id = (:classXId)
  // AND NOT cxks.knowledgeAndSkill.id IN (:knowledgeAndSkillIds)
  // """)
  // default void retainClassXKnowledgeAndSkills(
  // @Param("classXId") int classXId,
  // @Param("knowledgeAndSkillIds") Iterable<Integer> knowledgeAndSkillIds) {}

  @Query(
      """
          SELECT cxks
          FROM ClassXKnowledgeAndSkill cxks
          WHERE cxks.classX.id = (:class_x_id)
          """)
  List<ClassXKnowledgeAndSkill> selectAllByClassXId(@Param("class_x_id") int classXId);

  @Transactional
  default void setClassXKnoweldgeAndSkills(
      ClassX classX, List<KnowledgeAndSkill> knowledgeAndSkills) {
    List<ClassXKnowledgeAndSkill> current = selectAllByClassXId(classX.getId());
    Set<Integer> currentKnowledgeAndSkillIds =
        current.stream().map(e -> e.getKnowledgeAndSkill().getId()).collect(toSet());

    List<ClassXKnowledgeAndSkill> newKnowledgeAndSkills =
        knowledgeAndSkills.stream()
            .filter(e -> !currentKnowledgeAndSkillIds.contains(e.getId()))
            .map(e -> create(classX, e))
            .toList();
    saveAll(newKnowledgeAndSkills);

    Set<Integer> knowledgeAndSkillIds =
        knowledgeAndSkills.stream().map(e -> e.getId()).collect(toSet());
    List<ClassXKnowledgeAndSkill> toDelete =
        current.stream()
            .filter(e -> !knowledgeAndSkillIds.contains(e.getKnowledgeAndSkill().getId()))
            .toList();
    deleteAll(toDelete);
  }
}
