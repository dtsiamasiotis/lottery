CREATE TABLE IF NOT EXISTS participants(
    id BIGINT NOT NULL,
    msisdn BIGINT,
    first_seen TIMESTAMPTZ,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tickets(
    id BIGINT NOT NULL,
    numbers VARCHAR,

    PRIMARY KEY (id)
);