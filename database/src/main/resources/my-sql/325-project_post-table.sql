CREATE TABLE IF NOT EXISTS project_post
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    creation_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX (creation_time DESC),
    deleted          DATETIME,

    name             VARCHAR(255) NOT NULL,
    -- Deprecated. Use long_descr_html instead.
    message_html     TEXT,
    post_time        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX (post_time DESC),
    long_descr_html  MEDIUMTEXT,
    desired_feedback TEXT,
    being_edited     BOOLEAN,
    INDEX (being_edited),

    user_x_id        INT          NOT NULL,
    CONSTRAINT project_post__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_id       INT          NOT NULL,
    CONSTRAINT project_post__project_id
        FOREIGN KEY (project_id)
            REFERENCES project (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
