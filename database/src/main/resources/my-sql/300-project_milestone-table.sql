CREATE TABLE project_milestone
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    creation_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted         DATETIME,

    -- Indicates the relative position among a project's milestones.
    position        FLOAT        NOT NULL,

    name            VARCHAR(255) NOT NULL,
    short_descr     TEXT,
    long_descr_html TEXT,

    project_id      INT          NOT NULL,
    CONSTRAINT project_milestone__project_id
        FOREIGN KEY (project_id)
            REFERENCES project (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
