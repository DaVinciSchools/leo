CREATE TABLE IF NOT EXISTS assignment__knowledge_and_skill
(
    creation_time          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted                DATETIME,

    assignment_id          INT      NOT NULL,
    CONSTRAINT assignment__knowledge_and_skill__assignment_id
        FOREIGN KEY (assignment_id)
            REFERENCES assignment (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    knowledge_and_skill_id INT      NOT NULL,
    CONSTRAINT assignment__knowledge_and_skill__knowledge_and_skill_id
        FOREIGN KEY (knowledge_and_skill_id)
            REFERENCES knowledge_and_skill (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (assignment_id, knowledge_and_skill_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
