CREATE TABLE IF NOT EXISTS file_x
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    creation_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted       DATETIME,

    file_content  MEDIUMBLOB   NOT NULL,
    mime_type     VARCHAR(255) NOT NULL,

    user_x_id     INT          NOT NULL,
    CONSTRAINT file_x__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
