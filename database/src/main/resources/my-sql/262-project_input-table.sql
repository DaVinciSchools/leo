CREATE TABLE IF NOT EXISTS project_input
(
    id                        INT PRIMARY KEY AUTO_INCREMENT,
    creation_time             DATETIME                                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted                   DATETIME,

    -- TODO: Manage these with a task queue.
    timeout                   DATETIME,

    state                     ENUM ('PROCESSING', 'COMPLETED', 'FAILED') NOT NULL,

    existing_project_id       INT,
    -- CONSTRAINT project_input__existing_project_id is added in 999-project_input-table-addendum-FAILABLE.sql.

    existing_project_use_type ENUM ('USE_CONFIGURATION', 'MORE_LIKE_THIS', 'SUB_PROJECTS'),

    project_definition_id     INT                                        NOT NULL,
    CONSTRAINT project_input__project_definition_id
        FOREIGN KEY (project_definition_id)
            REFERENCES project_definition (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    assignment_id             INT,
    CONSTRAINT project_input__assignment_id
        FOREIGN KEY (assignment_id)
            REFERENCES assignment (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    -- Optional for demo usage.
    user_x_id                 INT,
    CONSTRAINT project_input__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
