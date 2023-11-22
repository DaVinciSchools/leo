CREATE TABLE IF NOT EXISTS project_definition
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    creation_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted       DATETIME,

    name          VARCHAR(255) NOT NULL,
    template      BOOLEAN,

    -- Optional for demo usage.
    user_x_id     INT,
    CONSTRAINT project_definition__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
