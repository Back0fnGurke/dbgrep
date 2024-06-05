CREATE TABLE IF NOT EXISTS account
(
    id              INTEGER                     NOT NULL UNIQUE,
    first_name      VARCHAR(255)                NOT NULL,
    last_name       VARCHAR(255)                NOT NULL,
    account_created DATE                        NOT NULL,
    last_updated    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_login      TIMESTAMP WITH TIME ZONE    NOT NULL,
    CONSTRAINT pk_account_id PRIMARY KEY (id)
);

INSERT INTO account (id, first_name, last_name, account_created, last_updated, last_login)
VALUES (1, 'Patrica', 'Bront', '2024-05-03', '2023-08-24 10:12:24', '2023-06-13 03:20:04+02');
INSERT INTO account (id, first_name, last_name, account_created, last_updated, last_login)
VALUES (2, 'Pietra', 'Hadgraft', '2023-12-31', '2023-11-24 10:39:07', '2023-06-13 03:20:04+02');
INSERT INTO account (id, first_name, last_name, account_created, last_updated, last_login)
VALUES (3, 'Ciro', 'Hornig', '2024-01-02', '2023-09-26 23:13:25', '2023-06-13 03:20:04+02');
INSERT INTO account (id, first_name, last_name, account_created, last_updated, last_login)
VALUES (4, 'Conni', 'Jennemann', '2023-06-13', '2024-05-04 11:06:29', '2023-06-13 03:20:04+02');
INSERT INTO account (id, first_name, last_name, account_created, last_updated, last_login)
VALUES (5, 'Estella', 'Markie', '2023-09-06', '2024-03-12 17:39:27', '2024-04-12 17:39:27+02');
INSERT INTO account (id, first_name, last_name, account_created, last_updated, last_login)
VALUES (6, 'Donielle', 'Eschalotte', '2023-07-20', '2023-09-24 16:14:18', '2023-06-13 03:20:04+02');
INSERT INTO account (id, first_name, last_name, account_created, last_updated, last_login)
VALUES (7, 'Yvonne', 'Biaggetti', '2024-01-04', '2023-09-19 10:09:52', '2023-06-13 03:20:04+02');
INSERT INTO account (id, first_name, last_name, account_created, last_updated, last_login)
VALUES (8, 'Hobart', 'Poulgreen', '2023-11-03', '2024-01-10 15:44:47', '2023-06-13 03:20:04+02');
INSERT INTO account (id, first_name, last_name, account_created, last_updated, last_login)
VALUES (9, 'Tye', 'Heintzsch', '2024-03-01', '2023-08-05 02:46:56', '2023-06-13 03:20:04+02');
INSERT INTO account (id, first_name, last_name, account_created, last_updated, last_login)
VALUES (10, 'Claudianus', 'Hirsch', '2023-10-22', '2023-08-04 11:02:02', '2023-06-13 03:20:04+02');