CREATE TRIGGER IF NOT EXISTS `delete_from.deadline_status.deadline_id`
AFTER UPDATE
ON deadline
FOR EACH ROW
  UPDATE deadline_status
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND deadline_id = NEW.id;
