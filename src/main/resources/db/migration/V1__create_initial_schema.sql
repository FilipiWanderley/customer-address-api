CREATE TABLE customers (
    id          BIGSERIAL       PRIMARY KEY,
    name        VARCHAR(100)    NOT NULL,
    email       VARCHAR(150)    NOT NULL UNIQUE,
    phone       VARCHAR(11)     NOT NULL,
    zip_code    VARCHAR(8)      NOT NULL,
    street      VARCHAR(200),
    number      VARCHAR(20),
    complement  VARCHAR(100),
    neighborhood VARCHAR(100),
    city        VARCHAR(100),
    state       VARCHAR(2),
    created_at  TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE TABLE cep_consultation_logs (
    id               BIGSERIAL    PRIMARY KEY,
    zip_code         VARCHAR(8)   NOT NULL,
    response_payload TEXT         NOT NULL,
    http_status      INTEGER      NOT NULL,
    success          BOOLEAN      NOT NULL,
    consulted_at     TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_customers_email       ON customers (email);
CREATE INDEX idx_cep_logs_zip_code     ON cep_consultation_logs (zip_code);
CREATE INDEX idx_cep_logs_consulted_at ON cep_consultation_logs (consulted_at DESC);
