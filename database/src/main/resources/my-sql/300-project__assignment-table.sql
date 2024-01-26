CREATE TABLE IF NOT EXISTS project__assignment
(
    creation_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted       DATETIME,

    project_id    INT      NOT NULL,
    CONSTRAINT project__assignment__project_id
        FOREIGN KEY (project_id)
            REFERENCES project (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    assignment_id INT      NOT NULL,
    CONSTRAINT project__assignment__assignment_id
        FOREIGN KEY (assignment_id)
            REFERENCES assignment (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (project_id, assignment_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
