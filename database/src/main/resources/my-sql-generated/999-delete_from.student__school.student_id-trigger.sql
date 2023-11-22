CREATE TRIGGER IF NOT EXISTS `delete_from.student__school.student_id`
AFTER UPDATE
ON student
FOR EACH ROW
  UPDATE student__school
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND student_id = NEW.id;
