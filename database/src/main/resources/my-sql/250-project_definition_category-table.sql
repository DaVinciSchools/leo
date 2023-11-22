CREATE TABLE IF NOT EXISTS project_definition_category
(
    id                                  INT PRIMARY KEY AUTO_INCREMENT,
    creation_time                       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted                             DATETIME,

    -- Indicates the relative position among Ikigai diagram inputs.
    -- The smallest value starts to the right and greater values rotate clockwise.
    position                            FLOAT    NOT NULL,
    max_num_values                      INT      NOT NULL,

    -- The type of category input collected.
    project_definition_category_type_id INT      NOT NULL,
    CONSTRAINT project_definition_category__project_definition_category_type_id
        FOREIGN KEY (project_definition_category_type_id)
            REFERENCES project_definition_category_type (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    -- The project definition that this is part of.
    project_definition_id               INT      NOT NULL,
    CONSTRAINT project_definition_category__project_definition_id
        FOREIGN KEY (project_definition_id)
            REFERENCES project_definition (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
