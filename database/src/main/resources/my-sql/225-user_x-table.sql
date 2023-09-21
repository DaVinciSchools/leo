-- 'user' is a SQL reserved word. So, append '_x'.
CREATE TABLE user_x
(
    id                            INT PRIMARY KEY AUTO_INCREMENT,
    creation_time                 DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted                       DATETIME,

    first_name                    VARCHAR(255)        NOT NULL,
    last_name                     VARCHAR(255)        NOT NULL,
    -- https://www.rfc-editor.org/errata_search.php?rfc=3696&eid=1690
    email_address                 VARCHAR(254) UNIQUE NOT NULL,
    INDEX                         user_x__email_address USING HASH (email_address),

    -- From org.springframework.security.crypto.factory.PasswordEncoderFactories.
    encoded_password              TEXT                NOT NULL,

    -- This is for a one time password recovery or a one time QR code sign in.
    temporary_password_good_until DATETIME,
    temporary_encoded_password    TEXT,

    district_id                   INT, -- Nullable for demo users.
    CONSTRAINT user_x__district_id
        FOREIGN KEY (district_id)
            REFERENCES district (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    admin_x_id                    INT UNIQUE,
    CONSTRAINT user_x__admin_x_id
        FOREIGN KEY (admin_x_id)
            REFERENCES admin_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    teacher_id                    INT UNIQUE,
    CONSTRAINT user_x__teacher_id
        FOREIGN KEY (teacher_id)
            REFERENCES teacher (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    student_id                    INT UNIQUE,
    CONSTRAINT user_x__student_id
        FOREIGN KEY (student_id)
            REFERENCES student (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    interest_id                   INT UNIQUE,
    CONSTRAINT user_x__interest_id
        FOREIGN KEY (interest_id)
            REFERENCES interest (id)
            ON DELETE SET NULL
            ON UPDATE CASCADE
) ENGINE InnoDB
  CHAR SET UTF8MB4;
