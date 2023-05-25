CREATE TABLE project_input
(
    id                    INT PRIMARY KEY AUTO_INCREMENT,
    creation_time         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- TODO: Manage these with a task queue.
    timeout               DATETIME,
    state                 ENUM ('PROCESSING', 'COMPLETED', 'FAILED') NOT NULL,

    project_definition_id INT,
    CONSTRAINT project_input__project_definition_id
        FOREIGN KEY (project_definition_id)
            REFERENCES project_definition (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    user_x_id            INT      NOT NULL,
    CONSTRAINT project_input__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
