CREATE TABLE knowledge_and_skill
(
    id                INT PRIMARY KEY AUTO_INCREMENT,

    title             VARCHAR(255)  NOT NULL,
    short_desc            VARCHAR(2048)  NOT NULL,
    short_descr_quill_zip BLOB,
    long_desc             VARCHAR(16384) NOT NULL,
    long_descr_quill_zip  BLOB,

    class_id          INT           NOT NULL,
    CONSTRAINT knowledge_and_skill_class_id
        FOREIGN KEY (class_id)
            REFERENCES class (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
