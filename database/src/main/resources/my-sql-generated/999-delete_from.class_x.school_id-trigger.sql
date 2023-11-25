CREATE TRIGGER IF NOT EXISTS `delete_from.class_x.school_id`
AFTER UPDATE
ON school
FOR EACH ROW
  UPDATE class_x
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND school_id = NEW.id;
