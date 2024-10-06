CREATE TABLE trucks
(
    id           SERIAL PRIMARY KEY,
    name_truck   VARCHAR(255) NOT NULL,
    name_parcels TEXT[]       NOT NULL,
    parcels      JSON         NOT NULL
);
