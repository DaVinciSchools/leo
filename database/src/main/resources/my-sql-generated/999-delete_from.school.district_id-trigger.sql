CREATE TRIGGER IF NOT EXISTS `delete_from.school.district_id`
AFTER UPDATE
ON district
FOR EACH ROW
  UPDATE school
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND district_id = NEW.id;
