CREATE SEQUENCE customer_seq START 1 INCREMENT 50;

CREATE TABLE customer
(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(100) DEFAULT NULL,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL
);

CREATE INDEX idx_customer_created ON customer(created);
CREATE INDEX idx_customer_updated ON customer(updated);
