CREATE TABLE assignment__project_definition
(
    creation_time         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    selected              BOOLEAN,

    assignment_id         INT      NOT NULL,
    CONSTRAINT assignment__project_definition__assignment_id
        FOREIGN KEY (assignment_id)
            REFERENCES assignment (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_definition_id INT      NOT NULL,
    CONSTRAINT assignment__project_definition__project_definition_id
        FOREIGN KEY (project_definition_id)
            REFERENCES project_definition (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (assignment_id, project_definition_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
