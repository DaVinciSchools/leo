CREATE TABLE IF NOT EXISTS deadline_source
(
    id                  INT PRIMARY KEY AUTO_INCREMENT,
    creation_time       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted             DATETIME,

    -- Inclusive.
    start_time          DATETIME      NOT NULL,
    -- Inclusive
    deadline_time       DATETIME      NOT NULL,

    name                VARCHAR(1024) NOT NULL,
    long_descr_html     TEXT,

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
    requirement_count   INT           NOT NULL DEFAULT 1,

    repeat_type         ENUM (
        -- Repeat by count.
        'COUNT',
        -- Repeat until end time.
        'END_TIME'),
    repeat_frequency_ms INT,
    -- A value of one means that there would be two total deadlines.
    repeat_count        INT,

    user_x_id           INT           NOT NULL,
    CONSTRAINT deadline_source__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    assignment_id       INT           NOT NULL,
    CONSTRAINT deadline_source__assignment_id
        FOREIGN KEY (assignment_id)
            REFERENCES assignment (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
