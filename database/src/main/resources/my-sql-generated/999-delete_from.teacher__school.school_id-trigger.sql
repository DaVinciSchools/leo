CREATE TRIGGER IF NOT EXISTS `delete_from.teacher__school.school_id`
AFTER UPDATE
ON school
FOR EACH ROW
  UPDATE teacher__school
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND school_id = NEW.id;
