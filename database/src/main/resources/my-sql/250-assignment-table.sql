CREATE TABLE assignment
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    creation_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted         DATETIME,

    name            VARCHAR(255) NOT NULL,
    short_descr     TEXT,
    long_descr_html TEXT,

    class_x_id      INT,
    CONSTRAINT assignment__class_x_id
        FOREIGN KEY (class_x_id)
            REFERENCES class_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
