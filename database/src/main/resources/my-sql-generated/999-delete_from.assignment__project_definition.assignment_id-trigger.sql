CREATE TRIGGER IF NOT EXISTS `delete_from.assignment__project_definition.assignment_id`
AFTER UPDATE
ON assignment
FOR EACH ROW
  UPDATE assignment__project_definition
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND assignment_id = NEW.id;
