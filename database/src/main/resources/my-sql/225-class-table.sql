CREATE TABLE class
(
    id                    INT PRIMARY KEY AUTO_INCREMENT,

    name                  VARCHAR(255)  NOT NULL,
    short_descr           VARCHAR(2048) NOT NULL,
    short_descr_quill_zip BLOB,
    long_descr            TEXT          NOT NULL,
    long_descr_quill_zip  BLOB,

    school_id             INT           NOT NULL,
    CONSTRAINT class_school_id
        FOREIGN KEY (school_id)
            REFERENCES school (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
