CREATE TRIGGER IF NOT EXISTS `delete_from.class_x__knowledge_and_skill.class_x_id`
AFTER UPDATE
ON class_x
FOR EACH ROW
  UPDATE class_x__knowledge_and_skill
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND class_x_id = NEW.id;
