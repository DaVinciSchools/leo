CREATE TABLE knowledge_and_skill
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    creation_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted         DATETIME,

    name            VARCHAR(255) NOT NULL,
    type            ENUM ('EKS', 'XQ_COMPETENCY') NOT NULL,
    short_descr     TEXT,
    long_descr_html TEXT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
