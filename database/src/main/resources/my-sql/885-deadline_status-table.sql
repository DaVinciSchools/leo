CREATE TABLE IF NOT EXISTS deadline_status
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    creation_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted       DATETIME,

    status        ENUM ( --
        'NONE',
        'TEACHER_TO_REVIEW',
        'STUDENT_TO_REVIEW',
        'DONE',
        'EXCUSED',
        'LATE')            NOT NULL,

    user_x_id     INT      NOT NULL,
    CONSTRAINT deadline_status__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    deadline_id   INT      NOT NULL,
    CONSTRAINT deadline_status__deadline_id
        FOREIGN KEY (deadline_id)
            REFERENCES deadline (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    -- These will point to the requirement fulfillment.
    -- The one populated will depend on deadline.requirement_type.

    assignment    BOOLEAN,

    project_id    INT,
    CONSTRAINT deadline_status__project_id
        FOREIGN KEY (project_id)
            REFERENCES project (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    post_id       INT,
    CONSTRAINT deadline_status__post_id
        FOREIGN KEY (post_id)
            REFERENCES post (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    comment_x_id  INT,
    CONSTRAINT deadline_status__comment_x_id
        FOREIGN KEY (comment_x_id)
            REFERENCES comment_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
