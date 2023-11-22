CREATE TRIGGER IF NOT EXISTS `delete_from.project_post_rating.knowledge_and_skill_id`
AFTER UPDATE
ON knowledge_and_skill
FOR EACH ROW
  UPDATE project_post_rating
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND knowledge_and_skill_id = NEW.id;
