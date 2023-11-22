CREATE TABLE IF NOT EXISTS class_x__knowledge_and_skill
(
    creation_time          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted                DATETIME,

    class_x_id             INT      NOT NULL,
    CONSTRAINT class_x__knowledge_and_skill__class_x_id
        FOREIGN KEY (class_x_id)
            REFERENCES class_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    knowledge_and_skill_id INT      NOT NULL,
    CONSTRAINT class_x__knowledge_and_skill__knowledge_and_skill_id
        FOREIGN KEY (knowledge_and_skill_id)
            REFERENCES knowledge_and_skill (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (class_x_id, knowledge_and_skill_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
