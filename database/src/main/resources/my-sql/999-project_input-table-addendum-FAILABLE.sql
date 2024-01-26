-- FAILABLE because there's no "IF EXISTS" for ADD CONSTRAINT.

ALTER TABLE project_input
    ADD CONSTRAINT project_input__existing_project_id
        FOREIGN KEY (existing_project_id)
            REFERENCES project (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT;
