CREATE TABLE project__image
(
    creation_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    selected      BOOLEAN,

    project_id    INT      NOT NULL,
    CONSTRAINT project__image__project_id
        FOREIGN KEY (project_id)
            REFERENCES project (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    image_id      INT      NOT NULL,
    CONSTRAINT project__image__image_id
        FOREIGN KEY (image_id)
            REFERENCES file_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (project_id, image_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
