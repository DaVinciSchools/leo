CREATE TRIGGER IF NOT EXISTS `delete_from.assignment__knowledge_and_skill.assignment_id`
AFTER UPDATE
ON assignment
FOR EACH ROW
  UPDATE assignment__knowledge_and_skill
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND assignment_id = NEW.id;
