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
    balance         DECIMAL      NOT NULL,
    votes           MEDIUMINT    NOT NULL,
    bought_books    BIGINT       NOT NULL,
    CONSTRAINT pk_account_id PRIMARY KEY (id)
);