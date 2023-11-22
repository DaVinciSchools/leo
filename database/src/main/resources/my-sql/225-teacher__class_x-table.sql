CREATE TABLE IF NOT EXISTS teacher__class_x
(
    creation_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted       DATETIME,

    teacher_id    INT      NOT NULL,
    CONSTRAINT teacher__class_x__teacher_id
        FOREIGN KEY (teacher_id)
            REFERENCES teacher (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    class_x_id    INT      NOT NULL,
    CONSTRAINT teacher__class_x__class_x_id
        FOREIGN KEY (class_x_id)
            REFERENCES class_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (teacher_id, class_x_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
