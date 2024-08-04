CREATE VIEW user_data AS
SELECT a.id, a.first_name, a.last_name, ad.street, ad.street_number, ad.city, ad.country
FROM account AS a
         JOIN address AS ad ON a.id = ad.id;