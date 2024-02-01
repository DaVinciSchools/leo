CREATE TABLE IF NOT EXISTS deadline_status
(
    id                    INT PRIMARY KEY AUTO_INCREMENT,
    creation_time         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted               DATETIME,

    status                ENUM ( --
        'NONE',
        'TEACHER_TO_REVIEW',
        'STUDENT_TO_REVIEW',
        'DONE',
        'EXCUSED',
        'LATE')                    NOT NULL,
    met_requirement_count INT      NOT NULL DEFAULT 0,

    user_x_id             INT      NOT NULL,
    CONSTRAINT deadline_status__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    deadline_id           INT      NOT NULL,
    CONSTRAINT deadline_status__deadline_id
        FOREIGN KEY (deadline_id)
            REFERENCES deadline (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
