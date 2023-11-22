CREATE TRIGGER IF NOT EXISTS `delete_from.project_input_value.project_definition_category_id`
AFTER UPDATE
ON project_definition_category
FOR EACH ROW
  UPDATE project_input_value
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND project_definition_category_id = NEW.id;
