CREATE TABLE project_definition_category_type
(
    id                INT PRIMARY KEY AUTO_INCREMENT,
    creation_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted           DATETIME,

    -- Displayed in the category selector.
    short_descr       VARCHAR(1024) NOT NULL,
    include_in_demo   BOOLEAN,

    -- Attributes for the visual display of the category in the diagram.
    -- Displayed at the top of the category circle.
    name              VARCHAR(255) NOT NULL,
    -- Text on the category circle before values are selected.
    hint              VARCHAR(255) NOT NULL,
    -- Text above the inputs to describe what's being entered.
    input_descr       VARCHAR(1024) NOT NULL,
    -- Text inside the field before it is entered / selected.
    input_placeholder VARCHAR(255) NOT NULL,

    -- Inputs for AI querying for projects.
    -- Prefix statement: e.g., "You want to demonstrate mastery in" ... followed by values.
    query_prefix      VARCHAR(255) NOT NULL,

    -- The type of value being stored.
    value_type        ENUM('FREE_TEXT',
        'EKS' /*knowledge_and_skill_value_id*/,
        'XQ_COMPETENCY' /*knowledge_and_skill_value_id*/,
        'MOTIVATION') NOT NULL
) ENGINE InnoDB
  CHAR SET UTF8MB4;
