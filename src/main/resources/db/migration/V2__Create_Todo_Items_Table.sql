CREATE TABLE todo_items
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT       NOT NULL,
    title        VARCHAR(255) NOT NULL,
    description  TEXT         NOT NULL,
    date         DATE         NOT NULL,
    time         TIME,
    priority     VARCHAR(20)  NOT NULL CHECK (priority IN ('HIGH', 'MEDIUM', 'LOW')),
    status       VARCHAR(20)  NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'COMPLETED')),
    notify       VARCHAR(5)   NOT NULL CHECK (notify IN ('YES', 'NO')),
    completed_at TIMESTAMP,
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP    NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES table_user (id) ON DELETE CASCADE
);