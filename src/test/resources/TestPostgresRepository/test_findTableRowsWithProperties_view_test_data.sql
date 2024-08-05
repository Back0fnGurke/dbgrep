CREATE TABLE IF NOT EXISTS account
(
    id
    INTEGER
    NOT
    NULL
    UNIQUE,
    first_name
    TEXT
    NOT
    NULL,
    last_name
    TEXT
    NOT
    NULL,
    account_created
    TIMESTAMP
    NOT
    NULL,
    username
    TEXT
    NOT
    NULL,
    email
    TEXT
    NOT
    NULL,
    password
    TEXT
    NOT
    NULL,
    CONSTRAINT
    pk_account_id
    PRIMARY
    KEY
(
    id
)
    );

CREATE VIEW user_name AS
SELECT first_name, last_name
FROM account;

INSERT INTO account (id, first_name, last_name, account_created, username, email, password)
VALUES (1, 'Dorian', 'Sporner', '2018-03-30 00:59:12', 'dsporner0', 'dsporner0@51.la', 'jY0\EZ&/9<X0.t');
INSERT INTO account (id, first_name, last_name, account_created, username, email, password)
VALUES (2, 'Bale', 'Sandal', '2018-06-14 01:06:41', 'bsandal1', 'bsandal1@virginia.edu', 'iO9"J?s==0b6cP}9');
INSERT INTO account (id, first_name, last_name, account_created, username, email, password)
VALUES (3, 'Burke', 'Klaves', '2022-12-31 14:43:42', 'bklaves2', 'bklaves2@opera.com', 'eA3\i$tyTe*M(/z');
INSERT INTO account (id, first_name, last_name, account_created, username, email, password)
VALUES (4, 'Caroline', 'Aubri', '2020-04-09 09:47:58', 'caubri3', 'caubri3@auda.org.au', 'gK7*2Eit');
INSERT INTO account (id, first_name, last_name, account_created, username, email, password)
VALUES (5, 'Andrea', 'Rummings', '2020-03-29 01:43:10', 'arummings4', 'arummings4@cnbc.com', 'iL7_~0m#~\*');
INSERT INTO account (id, first_name, last_name, account_created, username, email, password)
VALUES (6, 'Keenan', 'Ramme', '2019-01-07 09:11:25', 'kramme5', 'kramme5@vistaprint.com', 'aE9)*,3ye1?)Snh');
INSERT INTO account (id, first_name, last_name, account_created, username, email, password)
VALUES (7, 'Candace', 'Breslauer', '2019-02-05 15:18:33', 'cbreslauer6', 'cbreslauer6@hao123.com', 'tQ8/vr&.');
INSERT INTO account (id, first_name, last_name, account_created, username, email, password)
VALUES (8, 'Nelson', 'Santacrole', '2018-09-01 19:57:19', 'nsantacrole7', 'nsantacrole7@usatoday.com',
        'qP2#Z0.I1C@2kV');
INSERT INTO account (id, first_name, last_name, account_created, username, email, password)
VALUES (9, 'Laurel', 'Norrie', '2020-11-15 17:37:15', 'lnorrie8', 'lnorrie8@google.ru', 'mQ6}y=B8+eK');
INSERT INTO account (id, first_name, last_name, account_created, username, email, password)
VALUES (10, 'Vernen', 'Pordal', '2020-07-20 01:21:51', 'vpordal9', 'vpordal9@paginegialle.it', 'nZ4_k\+!N*xhT');
