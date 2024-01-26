CREATE TABLE IF NOT EXISTS project_post_comment
(
    id                         INT PRIMARY KEY AUTO_INCREMENT,
    creation_time              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX (creation_time DESC),
    deleted                    DATETIME,

    post_time                  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX (post_time DESC),
    -- Deprecated. Use long_descr_html instead.
    comment_html               TEXT,
    long_descr_html            MEDIUMTEXT,
    being_edited               BOOLEAN,
    INDEX (being_edited),

    positive_feedback          TEXT,
    how_improved_feedback      TEXT,
    to_improve_feedback        TEXT,
    has_enough_content_percent INT,
    incremental_post_summary   TEXT,
    feedback_response_html     TEXT,

    user_x_id                  INT      NOT NULL,
    CONSTRAINT project_post_comment__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_post_id            INT      NOT NULL,
    CONSTRAINT project_post_comment__project_post_id
        FOREIGN KEY (project_post_id)
            REFERENCES project_post (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
