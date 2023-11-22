CREATE TRIGGER IF NOT EXISTS `delete_to.user_x.admin_x_id`
AFTER UPDATE
ON user_x
FOR EACH ROW
  UPDATE admin_x
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND id = NEW.admin_x_id;
