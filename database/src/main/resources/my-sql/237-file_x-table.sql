CREATE TABLE IF NOT EXISTS file_x
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    creation_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted       DATETIME,

    file_name     VARCHAR(256),
    file_content  MEDIUMBLOB   NOT NULL,
    -- A random 15 byte key, encoded as base64.
    -- This is used to prevent users from guessing the file references.
    file_key      CHAR(20)     NOT NULL,
    mime_type     VARCHAR(255) NOT NULL,

    user_x_id     INT          NOT NULL,
    CONSTRAINT file_x__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
