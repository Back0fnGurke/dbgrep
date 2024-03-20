CREATE TABLE IF NOT EXISTS rating
(
    id
    uuid
    NOT
    NULL
    UNIQUE,
    game_id
    uuid
    NOT
    NULL,
    person_id
    uuid
    NOT
    NULL,
    symbol_id
    uuid
    NOT
    NULL,
    CONSTRAINT
    pk_rating_id
    PRIMARY
    KEY
(
    id
),
    CONSTRAINT fk_game_id FOREIGN KEY
(
    game_id
) REFERENCES game
(
    id
),
    CONSTRAINT fk_person_id FOREIGN KEY
(
    person_id
) REFERENCES person
(
    id
),
    CONSTRAINT fk_symbol_id FOREIGN KEY
(
    symbol_id
) REFERENCES symbol
(
    id
)
    );