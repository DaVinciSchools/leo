CREATE TABLE IF NOT EXISTS project__image
(
    creation_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted       DATETIME,

    -- The most recent timestamp is the selected project image.
    -- If this is non-null it means that it has been selected in the past.
    selected      DATETIME,

    project_id    INT      NOT NULL,
    CONSTRAINT project__image__project_id
        FOREIGN KEY (project_id)
            REFERENCES project (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    file_x_id     INT      NOT NULL,
    CONSTRAINT project__image__file_x_id
        FOREIGN KEY (file_x_id)
            REFERENCES file_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (project_id, file_x_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
