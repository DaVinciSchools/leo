CREATE TABLE project_post_rating
(
    id                     INT PRIMARY KEY AUTO_INCREMENT,
    creation_time          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted                DATETIME,

    rating                 INT      NOT NULL,

    rating_type            ENUM ('INITIAL_1_TO_5'
        -- Integer value from 1 to 5. Something like:
        -- 1 - No Evidence
        -- 2 - Attempted
        -- 3 - Emerging
        -- 4 - Proficient
        -- 5 - Advanced
        ) NOT NULL,

    long_descr_html        MEDIUMTEXT,

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

    knowledge_and_skill_id INT      NOT NULL,
    CONSTRAINT project_post_rating__knowledge_and_skill_id
        FOREIGN KEY (knowledge_and_skill_id)
            REFERENCES knowledge_and_skill (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
