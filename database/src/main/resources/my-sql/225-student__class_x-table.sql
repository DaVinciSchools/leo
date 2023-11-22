CREATE TABLE IF NOT EXISTS student__class_x
(
    creation_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted       DATETIME,

    student_id    INT      NOT NULL,
    CONSTRAINT student__class_x__student_id
        FOREIGN KEY (student_id)
            REFERENCES student (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    class_x_id    INT      NOT NULL,
    CONSTRAINT student__class_x__class_x_id
        FOREIGN KEY (class_x_id)
            REFERENCES class_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (student_id, class_x_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
