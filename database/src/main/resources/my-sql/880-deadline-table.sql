CREATE TABLE IF NOT EXISTS deadline
(
    id                  INT PRIMARY KEY AUTO_INCREMENT,
    creation_time       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted             DATETIME,

    title               VARCHAR(1024) NOT NULL,
    long_descr_html     TEXT,

    deadline            DATETIME      NOT NULL,
    -- The maximum datetime in which this deadline may appear (for filtering optimization).
    end_deadline_date   DATETIME      NOT NULL,
    requirement_type    ENUM (
        -- Deadline for completing an assignment.
        'ASSIGNMENT',
        -- Deadline for creating an assignment project.
        'PROJECT',
        -- Deadline for creating a project post.
        'PROJECT_POST',
        -- Deadline for creating a project comment.
        'PROJECT_POST_COMMENT')       NOT NULL,
    -- The number of items required for this deadline. E.g., 3 comments.
    requirement_count   INT           NOT NULL,

    repeat_type         ENUM (
        -- Repeat by count.
        'COUNT',
        -- Repeat until end date.
        'END_DATE'),
    repeat_frequency_ms INT,
    repeat_count        INT,
    repeat_end_date     DATETIME,

    user_x_id           INT           NOT NULL,
    CONSTRAINT deadline__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    assignment_id       INT           NOT NULL,
    CONSTRAINT deadline__assignment_id
        FOREIGN KEY (assignment_id)
            REFERENCES assignment (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
