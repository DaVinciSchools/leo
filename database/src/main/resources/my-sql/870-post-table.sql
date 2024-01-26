CREATE TABLE IF NOT EXISTS post
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    creation_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted         DATETIME,

    name            VARCHAR(255) NOT NULL,
    last_updated    DATETIME     NOT NULL,
    long_descr_html TEXT         NOT NULL,
    -- A plain text copy of long_descr_html for indexing.
    long_descr_text TEXT         NOT NULL,
    FULLTEXT INDEX (long_descr_text),

    user_x_id       INT          NOT NULL,
    CONSTRAINT post__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    post_to_user_x  INT          NOT NULL,
    CONSTRAINT post__post_to_user_x
        FOREIGN KEY (post_to_user_x)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_id      INT          NOT NULL,
    CONSTRAINT post__project_id
        FOREIGN KEY (project_id)
            REFERENCES project (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    -- TODO: Delete this column after migrating it.
    project_post_id INT,
    CONSTRAINT post__project_post_id
        FOREIGN KEY (project_post_id)
            REFERENCES project_post (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
