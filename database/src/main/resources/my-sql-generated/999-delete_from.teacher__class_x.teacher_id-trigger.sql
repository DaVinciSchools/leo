CREATE TRIGGER IF NOT EXISTS `delete_from.teacher__class_x.teacher_id`
AFTER UPDATE
ON teacher
FOR EACH ROW
  UPDATE teacher__class_x
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND teacher_id = NEW.id;
