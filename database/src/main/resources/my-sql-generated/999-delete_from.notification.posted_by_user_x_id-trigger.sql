CREATE TRIGGER IF NOT EXISTS `delete_from.notification.posted_by_user_x_id`
AFTER UPDATE
ON user_x
FOR EACH ROW
  UPDATE notification
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND posted_by_user_x_id = NEW.id;
