-- 'class' is a Java reserved word. So, append '_x'.
CREATE TABLE IF NOT EXISTS class_x
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    creation_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted         DATETIME,

    name            VARCHAR(255) NOT NULL,
    number          VARCHAR(255) NOT NULL,
    period          VARCHAR(16),
    grade           VARCHAR(16),
    short_descr     TEXT,
    long_descr_html TEXT,

    school_id       INT,
    CONSTRAINT class_x__school_id
        FOREIGN KEY (school_id)
            REFERENCES school (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
