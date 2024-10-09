CREATE TABLE IF NOT EXISTS trucks
(
    id           SERIAL PRIMARY KEY,
    name_truck   VARCHAR(255) NOT NULL,
    name_parcels VARCHAR(255) NOT NULL,
    parcels      TEXT         NOT NULL
);
