CREATE TRIGGER IF NOT EXISTS `delete_from.deadline.deadline_source_id`
AFTER UPDATE
ON deadline_source
FOR EACH ROW
  UPDATE deadline
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND deadline_source_id = NEW.id;
