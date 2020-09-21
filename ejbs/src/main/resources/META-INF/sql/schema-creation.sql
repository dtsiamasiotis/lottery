CREATE TABLE IF NOT EXISTS participants(
    id BIGINT NOT NULL,
    msisdn BIGINT,
    first_seen TIMESTAMPTZ,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS cron(
    id BIGINT NOT NULL,
    expression VARCHAR,
    command VARCHAR
);

CREATE TABLE IF NOT EXISTS tickets(
    id BIGINT NOT NULL,
    numbers VARCHAR,
    date_played TIMESTAMPTZ,
    ticket_id BIGINT,
    valid BOOLEAN,
    charged BOOLEAN,
    PRIMARY KEY (id),
    FOREIGN KEY (participant_id) REFERENCES participants(id)
);

CREATE SEQUENCE ticket_id_seq
START WITH 1
INCREMENT BY 1
CACHE 20;