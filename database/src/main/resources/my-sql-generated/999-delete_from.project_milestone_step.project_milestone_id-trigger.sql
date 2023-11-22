CREATE TRIGGER IF NOT EXISTS `delete_from.project_milestone_step.project_milestone_id`
AFTER UPDATE
ON project_milestone
FOR EACH ROW
  UPDATE project_milestone_step
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND project_milestone_id = NEW.id;
