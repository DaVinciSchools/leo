CREATE TABLE file_x
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    creation_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted       DATETIME,

    file_content  MEDIUMBLOB   NOT NULL,
    mime_type     VARCHAR(255) NOT NULL
) ENGINE InnoDB
  CHAR SET UTF8MB4;
