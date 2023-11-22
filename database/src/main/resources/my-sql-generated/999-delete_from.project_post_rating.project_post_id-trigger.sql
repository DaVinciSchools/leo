CREATE TRIGGER IF NOT EXISTS `delete_from.project_post_rating.project_post_id`
AFTER UPDATE
ON project_post
FOR EACH ROW
  UPDATE project_post_rating
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND project_post_id = NEW.id;
