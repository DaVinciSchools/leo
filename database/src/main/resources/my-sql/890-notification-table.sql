CREATE TABLE IF NOT EXISTS notification
(
    id                  INT PRIMARY KEY AUTO_INCREMENT,
    creation_time       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted             DATETIME,

    title               VARCHAR(1024) NOT NULL,
    long_descr_html     TEXT,
    link                VARCHAR(1024) NOT NULL,
    acknowledged        BOOLEAN       NOT NULL,
    post_date           DATETIME      NOT NULL,

    status              ENUM (
        -- Notification is pending.
        'PENDING',
        -- Snoozing until a given time.
        'SNOOZED',
        -- Notification is completed.
        'COMPLETED',
        -- Notification is hidden.
        'HIDDEN')                     NOT NULL,

    severity            ENUM (
        -- Informational only, low priority.
        'INFO',
        -- Normal priority notification.
        'NORMAL',
        -- Urgently needs to be addressed.
        'URGENT')                     NOT NULL,

    complete_by_date    DATETIME,
    completed_date      DATETIME,
    snoozed_until_date  DATETIME,

    posted_by_user_x_id INT,
    CONSTRAINT notification__posted_by_user_x_id
        FOREIGN KEY (posted_by_user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    posted_to_user_x_id INT           NOT NULL,
    CONSTRAINT notification__posted_to_user_x_id
        FOREIGN KEY (posted_to_user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    deadline_id         INT,
    CONSTRAINT notification__deadline_id
        FOREIGN KEY (deadline_id)
            REFERENCES deadline (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
