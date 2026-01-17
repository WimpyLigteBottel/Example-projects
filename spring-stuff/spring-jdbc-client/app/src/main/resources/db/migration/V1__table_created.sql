CREATE TABLE if not exists public.orders (
    order_id      BIGSERIAL PRIMARY KEY,
    total_amount  DOUBLE PRECISION NOT NULL,
    is_paid       BOOLEAN NOT NULL,
    version       BIGINT NOT NULL
);


CREATE TABLE if not exists public.order_items (
    id        BIGSERIAL PRIMARY KEY,
    order_id BIGSERIAL NOT NULL,
    item     VARCHAR(255) NOT NULL,

    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id)
        REFERENCES orders(order_id)
        ON DELETE CASCADE
);
