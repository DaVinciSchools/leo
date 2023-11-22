CREATE TRIGGER IF NOT EXISTS `delete_from.project__image.file_x_id`
AFTER UPDATE
ON file_x
FOR EACH ROW
  UPDATE project__image
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND file_x_id = NEW.id;
