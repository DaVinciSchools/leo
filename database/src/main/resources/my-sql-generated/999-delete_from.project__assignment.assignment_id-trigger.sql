CREATE TRIGGER IF NOT EXISTS `delete_from.project__assignment.assignment_id`
AFTER UPDATE
ON assignment
FOR EACH ROW
  UPDATE project__assignment
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND assignment_id = NEW.id;
