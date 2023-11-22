CREATE TRIGGER IF NOT EXISTS `delete_from.student__school.school_id`
AFTER UPDATE
ON school
FOR EACH ROW
  UPDATE student__school
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND school_id = NEW.id;
