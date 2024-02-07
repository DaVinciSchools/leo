-- 'admin' is a SQL reserved word. So, append '_x'.
CREATE TABLE IF NOT EXISTS admin_x
(
    id                   INT PRIMARY KEY AUTO_INCREMENT,
    creation_time        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted              DATETIME,

    cross_district_admin BOOLEAN
) ENGINE InnoDB
  CHAR SET UTF8MB4;
