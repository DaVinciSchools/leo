CREATE TABLE project_input_value
(
    id                             INT PRIMARY KEY AUTO_INCREMENT,
    creation_time                  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted                        DATETIME,

    -- TODO: Remove after refactor.
    -- Indicates the relative position among a project's input values.
    position                       FLOAT    NOT NULL,

    project_input_id               INT      NOT NULL,
    CONSTRAINT project_input_value__project_input_id
        FOREIGN KEY (project_input_id)
            REFERENCES project_input (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    -- TODO: Remove after refactor.
    project_input_category_id      INT,
    FOREIGN KEY (project_input_category_id)
        REFERENCES project_input_category (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT,

    -- TODO: Require non-null.
    project_definition_category_id INT,
    CONSTRAINT project_input_value__project_definition_category_id
        FOREIGN KEY (project_definition_category_id)
            REFERENCES project_definition_category (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    -- The type and which ONE field is set below is indicated in the project_definition_category_type.

    free_text_value                TEXT,

    knowledge_and_skill_value_id   INT,
    CONSTRAINT project_input_value__knowledge_and_skill_value_id
        FOREIGN KEY (knowledge_and_skill_value_id)
            REFERENCES knowledge_and_skill (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    motivation_value_id            INT,
    CONSTRAINT project_input_value__motivation_id
        FOREIGN KEY (motivation_value_id)
            REFERENCES motivation (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
