CREATE TRIGGER IF NOT EXISTS `delete_from.project_definition_category.project_definition_categ`
AFTER UPDATE
ON project_definition_category_type
FOR EACH ROW
  UPDATE project_definition_category
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND project_definition_category_type_id = NEW.id;
