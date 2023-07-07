-- 'admin' is a SQL reserved word. So, append '_x'.
CREATE TABLE admin_x
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    creation_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted       DATETIME
) ENGINE InnoDB
  CHAR SET UTF8MB4;
