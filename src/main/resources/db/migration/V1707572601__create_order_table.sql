-- Criação da tabela "orders"
CREATE TABLE orders (
    id UUID PRIMARY KEY,
    value NUMERIC(10, 2) NOT NULL,
    discount NUMERIC(10, 2) NOT NULL,
    total NUMERIC(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL
);