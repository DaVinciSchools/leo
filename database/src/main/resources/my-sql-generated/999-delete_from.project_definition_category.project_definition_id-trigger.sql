CREATE TRIGGER IF NOT EXISTS `delete_from.project_definition_category.project_definition_id`
AFTER UPDATE
ON project_definition
FOR EACH ROW
  UPDATE project_definition_category
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND project_definition_id = NEW.id;
