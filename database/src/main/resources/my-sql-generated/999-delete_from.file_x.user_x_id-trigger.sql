CREATE TRIGGER IF NOT EXISTS `delete_from.file_x.user_x_id`
AFTER UPDATE
ON user_x
FOR EACH ROW
  UPDATE file_x
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND user_x_id = NEW.id;
