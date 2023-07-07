CREATE TABLE project_post_comment
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    creation_time   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted         DATETIME,

    comment_html    TEXT,

    user_x_id       INT      NOT NULL,
    CONSTRAINT project_post_comment__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_post_id INT      NOT NULL,
    CONSTRAINT project_post_comment__project_post_id
        FOREIGN KEY (project_post_id)
            REFERENCES project_post (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
