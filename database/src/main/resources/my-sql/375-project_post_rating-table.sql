CREATE TABLE IF NOT EXISTS project_post_rating
(
    id                     INT PRIMARY KEY AUTO_INCREMENT,
    creation_time          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted                DATETIME,

    rating                 INT      NOT NULL,

    rating_type            ENUM (
        -- Integer value from 1 to 5. Something like:
        -- 1 - No Evidence
        -- 2 - Attempted
        -- 3 - Emerging
        -- 4 - Proficient
        -- 5 - Advanced
        'INITIAL_1_TO_5',

        -- Percent complete from 0 to 100 of a particular project input-goal.
        'GOAL_COMPLETE_PCT'
        )                           NOT NULL,

    long_descr_html        MEDIUMTEXT,
    goal_progress          TEXT,
    goal_remaining         TEXT,

    user_x_id              INT      NOT NULL,
    CONSTRAINT project_post_rating__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_post_id        INT      NOT NULL,
    CONSTRAINT project_post_rating__project_post_id
        FOREIGN KEY (project_post_id)
            REFERENCES project_post (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    -- Deprecated.
    knowledge_and_skill_id INT,
    CONSTRAINT project_post_rating__knowledge_and_skill_id
        FOREIGN KEY (knowledge_and_skill_id)
            REFERENCES knowledge_and_skill (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_input_fulfillment_id INT,
    CONSTRAINT project_post_rating__project_input_fulfillment_id
        FOREIGN KEY (project_input_fulfillment_id)
            REFERENCES project_input_fulfillment (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
