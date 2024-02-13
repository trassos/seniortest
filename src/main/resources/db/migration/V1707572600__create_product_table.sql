-- Criação da tabela "product" e adição de restrição de enum para a coluna "type"
CREATE TABLE product (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    type VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL
);