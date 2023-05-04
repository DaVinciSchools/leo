CREATE TABLE project
(
    id                INT PRIMARY KEY AUTO_INCREMENT,
    creation_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    name              VARCHAR(255) NOT NULL,
    short_descr       VARCHAR(1024),
    short_descr_quill TEXT,
    long_descr        MEDIUMTEXT,
    long_descr_quill  MEDIUMTEXT,
    thumbs_state      ENUM('THUMBS_UP', 'THUMBS_DOWN'),
    needs_review      TINYINT, -- Boolean 0 = false.

    project_input_id  INT,
    CONSTRAINT project__project_input_id
        FOREIGN KEY (project_input_id)
            REFERENCES project_input (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
