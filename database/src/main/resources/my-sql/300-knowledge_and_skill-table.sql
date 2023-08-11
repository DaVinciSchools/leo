CREATE TABLE knowledge_and_skill
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    creation_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted         DATETIME,

    name            VARCHAR(255) NOT NULL,
    type            ENUM ('EKS', 'XQ_COMPETENCY') NOT NULL,
    category        VARCHAR(255),
    short_descr     TEXT,
    long_descr_html TEXT,
    global          BOOLEAN,

    user_x_id       INT NOT NULL,
    CONSTRAINT knowledge_and_skill__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
