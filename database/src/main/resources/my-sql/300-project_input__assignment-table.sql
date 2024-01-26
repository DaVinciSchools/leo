CREATE TABLE IF NOT EXISTS project_input__assignment
(
    creation_time    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted          DATETIME,

    project_input_id INT      NOT NULL,
    CONSTRAINT project_input__assignment__project_input_id
        FOREIGN KEY (project_input_id)
            REFERENCES project_input (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    assignment_id    INT      NOT NULL,
    CONSTRAINT project_input__assignment__assignment_id
        FOREIGN KEY (assignment_id)
            REFERENCES assignment (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (project_input_id, assignment_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
