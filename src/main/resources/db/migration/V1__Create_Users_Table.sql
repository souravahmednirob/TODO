CREATE TABLE table_user
(
    id         BIGSERIAL PRIMARY KEY,
    mail       VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL
);