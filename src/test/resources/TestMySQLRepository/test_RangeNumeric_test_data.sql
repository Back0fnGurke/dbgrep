CREATE TABLE IF NOT EXISTS account
(
    id
    INTEGER
    NOT
    NULL
    UNIQUE,
    first_name
    VARCHAR
(
    255
) NOT NULL,
    last_name VARCHAR
(
    255
) NOT NULL,
    account_created TIMESTAMP NOT NULL,
    username VARCHAR
(
    255
) NOT NULL,
    email VARCHAR
(
    255
) NOT NULL,
    password VARCHAR
(
    255
) NOT NULL,
    zipcode SMALLINT NOT NULL,
    balance DECIMAL NOT NULL,
    votes MEDIUMINT NOT NULL,
    bought_books BIGINT NOT NULL,
    CONSTRAINT pk_account_id PRIMARY KEY
(
    id
)
    );

INSERT INTO account (id, first_name, last_name, account_created, username, email, password, zipcode, balance, votes,
                     bought_books)
VALUES (1, 'Patrica', 'Bront', '2024-03-29 19:42:18', 'pbront0', 'pbront0@1und1.de', 'uG3<"Ko}TB', 255, 66878899,
        536177,
        487200);
INSERT INTO account (id, first_name, last_name, account_created, username, email, password, zipcode, balance, votes,
                     bought_books)
VALUES (2, 'Pietra', 'Hadgraft', '2023-12-31 09:40:43', 'phadgraft1', 'phadgraft1@photobucket.com', 'bT1$RtgY', 20337,
        28325172,
        915638, 788503);
INSERT INTO account (id, first_name, last_name, account_created, username, email, password, zipcode, balance, votes,
                     bought_books)
VALUES (3, 'Ciro', 'Hornig', '2024-01-02 18:14:07', 'chornig2', 'chornig2@archive.org', 'yQ4''Ok7{Np_rx0@7', 351,
        37569111,
        653734, 746122);
INSERT INTO account (id, first_name, last_name, account_created, username, email, password, zipcode, balance, votes,
                     bought_books)
VALUES (4, 'Conni', 'Jennemann', '2024-05-03 11:06:29', 'cjennemann3', 'cjennemann3@whitehouse.gov', 'sJ3@y)c|`w{ku',
        88,
        1958874, 474797, 546353);
INSERT INTO account (id, first_name, last_name, account_created, username, email, password, zipcode, balance, votes,
                     bought_books)
VALUES (5, 'Estella', 'Markie', '2023-09-06 16:17:15', 'emarkie4', 'emarkie4@technorati.com', 'fN9~Z6(1.U', 4,
        8526236, 530320,
        115828);
INSERT INTO account (id, first_name, last_name, account_created, username, email, password, zipcode, balance, votes,
                     bought_books)
VALUES (6, 'Donielle', 'Eschalotte', '2023-07-20 03:50:18', 'deschalotte5', 'deschalotte5@blogs.com', 'aI5''lbuH~D7',
        1,
        63701187, 465807, 335045);
INSERT INTO account (id, first_name, last_name, account_created, username, email, password, zipcode, balance, votes,
                     bought_books)
VALUES (7, 'Yvonne', 'Biaggetti', '2024-01-04 01:24:35', 'ybiaggetti6', 'ybiaggetti6@ning.com', 'bB5$4''''AkxnC>uK(',
        11247,
        94935074, 479981, 107847);
INSERT INTO account (id, first_name, last_name, account_created, username, email, password, zipcode, balance, votes,
                     bought_books)
VALUES (8, 'Hobart', 'Poulgreen', '2023-11-03 21:11:08', 'hpoulgreen7', 'hpoulgreen7@free.fr', 'yA7*ZO=P', 3546,
        69104476,
        861661, 432427);
INSERT INTO account (id, first_name, last_name, account_created, username, email, password, zipcode, balance, votes,
                     bought_books)
VALUES (9, 'Tye', 'Heintzsch', '2024-03-01 18:11:51', 'theintzsch8', 'theintzsch8@newyorker.com', 'hE1#zsv=P0', 24442,
        96443590,
        827467, 928780);
INSERT INTO account (id, first_name, last_name, account_created, username, email, password, zipcode, balance, votes,
                     bought_books)
VALUES (10, 'Claudianus', 'Hirsch', '2023-10-22 13:53:38', 'chirsch9', 'chirsch9@fc2.com', 'fR1`C=>yQCP', 618,
        97278675, 316616,
        358568);