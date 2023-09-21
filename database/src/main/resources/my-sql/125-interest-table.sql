CREATE TABLE interest
(
    id                  INT PRIMARY KEY AUTO_INCREMENT,
    creation_time       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted             DATETIME,

    first_name          VARCHAR(255) NOT NULL,
    last_name           VARCHAR(255) NOT NULL,
    -- https://www.rfc-editor.org/errata_search.php?rfc=3696&eid=1690
    email_address       VARCHAR(254) NOT NULL,
    profession          VARCHAR(255) NOT NULL,
    reason_for_interest TEXT         NOT NULL,

    district_name       VARCHAR(255),
    school_name         VARCHAR(255),

    address_line_1      VARCHAR(255),
    address_line_2      VARCHAR(255),
    city                VARCHAR(20),
    state               VARCHAR(2),
    zip_code            VARCHAR(10),

    num_teachers        INT,
    num_students        INT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
