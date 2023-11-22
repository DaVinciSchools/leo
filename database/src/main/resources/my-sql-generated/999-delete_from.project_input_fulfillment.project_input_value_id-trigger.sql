CREATE TRIGGER IF NOT EXISTS `delete_from.project_input_fulfillment.project_input_value_id`
AFTER UPDATE
ON project_input_value
FOR EACH ROW
  UPDATE project_input_fulfillment
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND project_input_value_id = NEW.id;
