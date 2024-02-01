CREATE TABLE IF NOT EXISTS deadline
(
    id                    INT PRIMARY KEY AUTO_INCREMENT,
    creation_time         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted               DATETIME,

    -- A zero based value of the index number of this deadline created by the
    -- deadline_source. E.g., a value of 1 would be the first repeat.
    source_deadline_index INT      NOT NULL DEFAULT 0,

    -- Inclusive.
    start_time            DATETIME NOT NULL,
    -- Inclusive
    deadline_time         DATETIME NOT NULL,

    deadline_source_id    INT      NOT NULL,
    CONSTRAINT deadline__deadline_source_id
        FOREIGN KEY (deadline_source_id)
            REFERENCES deadline_source (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
