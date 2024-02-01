CREATE TRIGGER IF NOT EXISTS `delete_from.deadline_source.assignment_id`
AFTER UPDATE
ON assignment
FOR EACH ROW
  UPDATE deadline_source
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND assignment_id = NEW.id;
