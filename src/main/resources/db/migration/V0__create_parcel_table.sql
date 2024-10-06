CREATE TABLE parcels
(
    id     SERIAL PRIMARY KEY,
    name   VARCHAR(255),
    symbol CHAR(1),
    form   JSONB
);