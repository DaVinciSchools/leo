CREATE TRIGGER IF NOT EXISTS `delete_from.comment_x.project_id`
AFTER UPDATE
ON project
FOR EACH ROW
  UPDATE comment_x
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND project_id = NEW.id;
