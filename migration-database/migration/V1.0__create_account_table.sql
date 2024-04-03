CREATE TABLE IF NOT EXISTS account
(
    id VARCHAR(255) NOT NULL UNIQUE,
    username TEXT NOT NULL,
    password TEXT NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    CONSTRAINT pk_account_id PRIMARY KEY (id)
);