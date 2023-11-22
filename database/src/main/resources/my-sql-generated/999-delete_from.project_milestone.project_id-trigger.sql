CREATE TRIGGER IF NOT EXISTS `delete_from.project_milestone.project_id`
AFTER UPDATE
ON project
FOR EACH ROW
  UPDATE project_milestone
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND project_id = NEW.id;
