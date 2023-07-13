CREATE TABLE project_input_category
(
    id                    INT PRIMARY KEY AUTO_INCREMENT,
    creation_time         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted               DATETIME,

    short_descr           VARCHAR(255) NOT NULL,

    -- Indicates the relative position among Ikigai diagram inputs.
    -- The smallest value starts to the right and greater values rotate clockwise.
    position              FLOAT        NOT NULL,

    -- Attributes for the visual display of the category in the diagram.
    -- Displayed at the top of the category circle.
    name                  VARCHAR(255) NOT NULL,
    -- Text on the category circle before values are selected.
    hint                  VARCHAR(255) NOT NULL,
    -- Text above the inputs to describe what's being entered.
    input_descr           VARCHAR(255) NOT NULL,
    -- Text inside the field before it is entered / selected.
    input_placeholder     VARCHAR(255) NOT NULL,

    -- Inputs for AI querying for projects.
    -- Prefix statement: e.g., "You want to demonstrate mastery in" ... followed by values.
    query_prefix          VARCHAR(255) NOT NULL,

    -- The type of value being stored.
    value_type            ENUM('FREE_TEXT',
        'EKS' /*knowledge_and_skill_value_id*/,
        'XQ_COMPETENCY' /*knowledge_and_skill_value_id*/,
        'MOTIVATION') NOT NULL,
    max_num_values        INT          NOT NULL,

    -- The project definition that this is part of.
    project_definition_id INT          NOT NULL,
    CONSTRAINT project_input_category__project_definition_id
        FOREIGN KEY (project_definition_id)
            REFERENCES project_definition (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
