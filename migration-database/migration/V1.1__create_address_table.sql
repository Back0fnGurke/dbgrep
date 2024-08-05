create table address
(
    id            INTEGER,
    account_id    INTEGER,
    street        VARCHAR(50),
    street_number VARCHAR(50),
    city          VARCHAR(50),
    country       VARCHAR(50),
    country_code  VARCHAR(50),
    postal_code   VARCHAR(50),
    CONSTRAINT pk_address_id PRIMARY KEY (id),
    CONSTRAINT fk_address_account_id FOREIGN KEY (account_id) REFERENCES account (id)
);
insert into address (id, account_id, street, street_number, city, country, country_code, postal_code)
values (1, 1, 'Center', '0', 'Huiyuan', 'China', 'CN', null);
insert into address (id, account_id, street, street_number, city, country, country_code, postal_code)
values (2, 2, 'Bunting', '549', 'Teseney', 'Eritrea', 'ER', null);
insert into address (id, account_id, street, street_number, city, country, country_code, postal_code)
values (3, 3, 'Randy', '77', 'Baião', 'Portugal', 'PT', '4640-107');
insert into address (id, account_id, street, street_number, city, country, country_code, postal_code)
values (4, 4, 'Katie', '8', 'Liuduzhai', 'China', 'CN', null);
insert into address (id, account_id, street, street_number, city, country, country_code, postal_code)
values (5, 5, 'Derek', '95', 'Lau', 'Nigeria', 'NG', null);
insert into address (id, account_id, street, street_number, city, country, country_code, postal_code)
values (6, 6, 'Sunfield', '70430', 'Chishui', 'China', 'CN', null);
insert into address (id, account_id, street, street_number, city, country, country_code, postal_code)
values (7, 7, 'Vermont', '8909', 'Baclayon', 'Philippines', 'PH', '6301');
insert into address (id, account_id, street, street_number, city, country, country_code, postal_code)
values (8, 8, 'Eliot', '60603', 'Stepnogorsk', 'Kazakhstan', 'KZ', null);
insert into address (id, account_id, street, street_number, city, country, country_code, postal_code)
values (9, 9, 'Surrey', '36303', 'Fenggang', 'China', 'CN', null);
insert into address (id, account_id, street, street_number, city, country, country_code, postal_code)
values (10, 10, 'Monterey', '4636', 'Ad Dann', 'Yemen', 'YE', null);

