CREATE TABLE project
(
    id                  INT PRIMARY KEY AUTO_INCREMENT,
    creation_time       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    name                VARCHAR(255) NOT NULL,

    short_descr         VARCHAR(1024),
    short_descr_quill   TEXT,
    long_descr          MEDIUMTEXT,
    long_descr_quill    MEDIUMTEXT,
    steps_descr         MEDIUMTEXT,
    steps_descr_quill   MEDIUMTEXT,

    generator           MEDIUMTEXT,

    favorite            BOOLEAN,
    thumbs_state        ENUM('THUMBS_UP', 'THUMBS_DOWN'),
    thumbs_state_reason TEXT,
    archived            BOOLEAN,
    deleted             BOOLEAN,
    needs_review        BOOLEAN,
    active              BOOLEAN,

    project_input_id    INT,
    CONSTRAINT project__project_input_id
        FOREIGN KEY (project_input_id)
            REFERENCES project_input (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
