CREATE TRIGGER IF NOT EXISTS `delete_from.assignment.class_x_id`
AFTER UPDATE
ON class_x
FOR EACH ROW
  UPDATE assignment
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND class_x_id = NEW.id;
