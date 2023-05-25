CREATE TABLE project_definition
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    creation_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    name          VARCHAR(255) NOT NULL,
    template      BOOLEAN
) ENGINE InnoDB
  CHAR SET UTF8MB4;
