CREATE TABLE tag
(
    id                      INT PRIMARY KEY AUTO_INCREMENT,
    creation_time           DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted                 DATETIME,

    text                    VARCHAR(32) NOT NULL,

    user_x_id               INT NOT NULL,
    CONSTRAINT tags__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_id              INT,
    CONSTRAINT tags__project_id
        FOREIGN KEY (project_id)
            REFERENCES project (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_post_id         INT,
    CONSTRAINT tags__project_post_id
        FOREIGN KEY (project_post_id)
            REFERENCES project_post (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_post_comment_id INT,
    CONSTRAINT tags__project_post_comment_id
        FOREIGN KEY (project_post_comment_id)
            REFERENCES project_post_comment (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
