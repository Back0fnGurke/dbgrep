CREATE TABLE IF NOT EXISTS account
(
    id              INTEGER      NOT NULL UNIQUE,
    first_name      VARCHAR(255) NOT NULL,
    last_name       VARCHAR(255) NOT NULL,
    account_created DATE         NOT NULL,
    username        VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    zipcode         SMALLINT     NOT NULL,
    balance         MONEY        NOT NULL,
    votes           BIGINT       NOT NULL,
    bought_books    NUMERIC      NOT NULL,
    CONSTRAINT pk_account_id PRIMARY KEY (id)
);