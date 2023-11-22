CREATE TRIGGER IF NOT EXISTS `delete_from.assignment__project_definition.project_definition_id`
AFTER UPDATE
ON project_definition
FOR EACH ROW
  UPDATE assignment__project_definition
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND project_definition_id = NEW.id;
