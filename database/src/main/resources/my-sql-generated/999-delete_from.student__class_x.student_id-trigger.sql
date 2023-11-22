CREATE TRIGGER IF NOT EXISTS `delete_from.student__class_x.student_id`
AFTER UPDATE
ON student
FOR EACH ROW
  UPDATE student__class_x
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND student_id = NEW.id;
