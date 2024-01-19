CREATE TRIGGER IF NOT EXISTS `delete_from.comment_x.post_id`
AFTER UPDATE
ON post
FOR EACH ROW
  UPDATE comment_x
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND post_id = NEW.id;
