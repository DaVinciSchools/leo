CREATE TRIGGER IF NOT EXISTS `delete_from.assignment__knowledge_and_skill.knowledge_and_skill_`
AFTER UPDATE
ON knowledge_and_skill
FOR EACH ROW
  UPDATE assignment__knowledge_and_skill
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND knowledge_and_skill_id = NEW.id;
