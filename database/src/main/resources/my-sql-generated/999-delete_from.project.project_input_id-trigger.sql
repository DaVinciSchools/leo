CREATE TRIGGER IF NOT EXISTS `delete_from.project.project_input_id`
AFTER UPDATE
ON project_input
FOR EACH ROW
  UPDATE project
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND project_input_id = NEW.id;
