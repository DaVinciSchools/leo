CREATE TABLE project_post
(
    id                   INT PRIMARY KEY AUTO_INCREMENT,

    title                VARCHAR(255) NOT NULL,
    short_desc            VARCHAR(2048)  NOT NULL,
    short_descr_quill_zip BLOB,
    long_desc             VARCHAR(16384) NOT NULL,
    long_descr_quill_zip  BLOB,

    post_time_micros_utc BIGINT       NOT NULL,

    user_id              INT          NOT NULL,
    CONSTRAINT project_post_user_id
        FOREIGN KEY (user_id)
            REFERENCES user (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
