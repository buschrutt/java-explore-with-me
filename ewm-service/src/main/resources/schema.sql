DROP TABLE IF EXISTS stats CASCADE;

CREATE TABLE IF NOT EXISTS test (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    test VARCHAR(64)
);