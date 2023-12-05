CREATE TRIGGER IF NOT EXISTS `delete_from.project_post_rating.project_input_fulfillment_id`
AFTER UPDATE
ON project_input_fulfillment
FOR EACH ROW
  UPDATE project_post_rating
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND project_input_fulfillment_id = NEW.id;
