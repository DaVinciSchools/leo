CREATE TRIGGER IF NOT EXISTS `delete_to.user_x.teacher_id`
    AFTER UPDATE
    ON user_x
    FOR EACH ROW
    UPDATE teacher
    SET deleted = NEW.deleted
    WHERE deleted IS NULL
      AND NEW.deleted IS NOT NULL
      AND id = NEW.teacher_id;

