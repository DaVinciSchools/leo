CREATE TABLE log
(
    id                    INT PRIMARY KEY AUTO_INCREMENT,
    creation_time         DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    user_x_id             INT,
    status                ENUM('ERROR', 'SUCCESS') NOT NULL,
    notes                 MEDIUMTEXT,

    caller                MEDIUMTEXT,
    request               MEDIUMTEXT NOT NULL,
    request_type          MEDIUMTEXT NOT NULL,
    request_time          DATETIME   NOT NULL,

    -- The time of the first response, presumably from external services.
    --
    -- Base 64 mime-encoded if the type is binary. If it is a UTF-8 string
    -- in binary format, it will be converted to a string.
    initial_response      MEDIUMTEXT,
    initial_response_type MEDIUMTEXT,
    initial_response_time DATETIME   NOT NULL,

    -- The time of a fully processed response to send back to the client.
    final_response        MEDIUMTEXT,
    final_response_type   MEDIUMTEXT,
    final_response_time   DATETIME   NOT NULL,

    -- Error information.
    stack_trace           MEDIUMTEXT,
    last_input            MEDIUMTEXT,
    last_input_type       MEDIUMTEXT,
    last_input_time       DATETIME,

    CONSTRAINT log__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
