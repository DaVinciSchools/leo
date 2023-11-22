CREATE TABLE IF NOT EXISTS project_input_fulfillment
(
    id                     INT PRIMARY KEY AUTO_INCREMENT,
    creation_time          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted                DATETIME,

    project_id             INT      NOT NULL,
    CONSTRAINT project_input_fulfillment__project_id
        FOREIGN KEY (project_id)
            REFERENCES project (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_input_value_id INT      NOT NULL,
    CONSTRAINT project_input_fulfillment__project_input_value_id
        FOREIGN KEY (project_input_value_id)
            REFERENCES project_input_value (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    how_project_fulfills   TEXT     NOT NULL,
    fulfillment_percentage INT      NOT NULL,
    CONSTRAINT project_input_fulfillment__percentage
        CHECK (fulfillment_percentage >= 0 AND fulfillment_percentage <= 100),

    visible_indicator      TEXT     NOT NULL
) ENGINE InnoDB
  CHAR SET UTF8MB4;
