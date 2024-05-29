CREATE TABLE IF NOT EXISTS account
(
    id              INTEGER      NOT NULL UNIQUE,
    first_name      VARCHAR(255) NOT NULL,
    last_name       VARCHAR(255) NOT NULL,
    account_created DATE         NOT NULL,
    last_updated    DATETIME     NOT NULL,
    last_login      TIMESTAMP    NOT NULL,
    CONSTRAINT pk_account_id PRIMARY KEY (id)
);