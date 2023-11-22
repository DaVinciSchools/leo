CREATE TRIGGER IF NOT EXISTS `delete_to.user_x.student_id`
AFTER UPDATE
ON user_x
FOR EACH ROW
  UPDATE student
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND id = NEW.student_id;
