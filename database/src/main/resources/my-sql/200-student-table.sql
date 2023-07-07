CREATE TABLE student
(
    id                  INT PRIMARY KEY AUTO_INCREMENT,
    creation_time       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted             DATETIME,

    district_student_id INT,
    grade               INT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
