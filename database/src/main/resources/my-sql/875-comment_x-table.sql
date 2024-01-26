-- 'comment' is a SQL reserved word. So, append '_x'.
CREATE TABLE IF NOT EXISTS comment_x
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    creation_time   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted         DATETIME,

    last_updated    DATETIME NOT NULL,
    long_descr_html TEXT     NOT NULL,
    -- A plain text copy of long_descr_html for indexing.
    long_descr_text TEXT     NOT NULL,
    FULLTEXT INDEX (long_descr_text),

    user_x_id       INT      NOT NULL,
    CONSTRAINT comment__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    -- The comment is going to be attached to one of the following.

    post_id         INT,
    CONSTRAINT comment__post_id
        FOREIGN KEY (post_id)
            REFERENCES post (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_id      INT,
    CONSTRAINT comment__project_id
        FOREIGN KEY (project_id)
            REFERENCES project (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
