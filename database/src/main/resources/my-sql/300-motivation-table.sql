CREATE TABLE IF NOT EXISTS motivation
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    creation_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted         DATETIME,

    name            VARCHAR(255) NOT NULL,
    short_descr     TEXT,
    long_descr_html TEXT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
