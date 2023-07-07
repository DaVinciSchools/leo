CREATE TABLE project_input_value
(
    id                           INT PRIMARY KEY AUTO_INCREMENT,
    creation_time                DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted                      DATETIME,

    project_input_id             INT      NOT NULL,
    FOREIGN KEY (project_input_id)
        REFERENCES project_input (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT,

    project_input_category_id    INT      NOT NULL,
    FOREIGN KEY (project_input_category_id)
        REFERENCES project_input_category (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT,

    -- Indicates the relative sort order of the values.
    position                     INT      NOT NULL,

    -- The type and which ONE field is set below is indicated in the project_input_category.

    free_text_value              TEXT,

    knowledge_and_skill_value_id INT,
    CONSTRAINT project_input_value__knowledge_and_skill_value_id
        FOREIGN KEY (knowledge_and_skill_value_id)
            REFERENCES knowledge_and_skill (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    motivation_value_id          INT,
    CONSTRAINT project_input_value__motivation_id
        FOREIGN KEY (motivation_value_id)
            REFERENCES motivation (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
