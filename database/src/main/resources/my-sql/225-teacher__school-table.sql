CREATE TABLE IF NOT EXISTS teacher__school
(
    creation_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted       DATETIME,

    teacher_id    INT      NOT NULL,
    CONSTRAINT teacher__school__teacher_id
        FOREIGN KEY (teacher_id)
            REFERENCES teacher (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    school_id     INT      NOT NULL,
    CONSTRAINT teacher__school__school_id
        FOREIGN KEY (school_id)
            REFERENCES school (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (teacher_id, school_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
