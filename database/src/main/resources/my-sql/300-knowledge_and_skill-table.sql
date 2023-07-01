CREATE TABLE knowledge_and_skill
(
    id                INT PRIMARY KEY AUTO_INCREMENT,
    creation_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    name              VARCHAR(255) NOT NULL,
    type              ENUM ('EKS', 'XQ_COMPETENCY') NOT NULL,
    short_descr       TEXT,
    short_descr_quill TEXT,
    long_descr        TEXT,
    long_descr_quill  TEXT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
