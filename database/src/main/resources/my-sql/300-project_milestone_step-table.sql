CREATE TABLE project_milestone_step
(
    id                   INT PRIMARY KEY AUTO_INCREMENT,
    creation_time        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Indicates the relative location in the milestone.
    position             INT          NOT NULL,

    name                 VARCHAR(255) NOT NULL,
    short_descr          TEXT,
    short_descr_quill    TEXT,
    long_descr           TEXT,
    long_descr_quill     TEXT,

    project_milestone_id INT          NOT NULL,
    CONSTRAINT project_milestone_step__project_milestone_id
        FOREIGN KEY (project_milestone_id)
            REFERENCES project_milestone (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
