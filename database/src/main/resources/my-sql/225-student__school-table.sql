CREATE TABLE IF NOT EXISTS student__school
(
    creation_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted       DATETIME,

    student_id    INT      NOT NULL,
    CONSTRAINT student__school__student_id
        FOREIGN KEY (student_id)
            REFERENCES student (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    school_id     INT      NOT NULL,
    CONSTRAINT student__school__school_id
        FOREIGN KEY (school_id)
            REFERENCES school (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (student_id, school_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
