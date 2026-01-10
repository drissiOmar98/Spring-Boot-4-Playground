-- Coffee table
CREATE TABLE IF NOT EXISTS coffee (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    size VARCHAR(20) NOT NULL,
    CONSTRAINT coffee_price_positive CHECK (price > 0),
    CONSTRAINT coffee_size_valid CHECK (size IN ('SMALL', 'MEDIUM', 'LARGE'))
    );

CREATE INDEX IF NOT EXISTS idx_coffee_name ON coffee(name);
CREATE INDEX IF NOT EXISTS idx_coffee_size ON coffee(size);
CREATE INDEX IF NOT EXISTS idx_coffee_price ON coffee(price);

-- Orders table
CREATE TABLE IF NOT EXISTS orders (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      customer_id BIGINT,
                                      customer_name VARCHAR(200) NOT NULL,
    order_date TIMESTAMP NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT orders_total_positive CHECK (total_amount >= 0),
    CONSTRAINT orders_status_valid CHECK (status IN ('PENDING', 'PREPARING', 'READY', 'DELIVERED', 'CANCELLED'))
    );

CREATE INDEX IF NOT EXISTS idx_orders_customer_name ON orders(customer_name);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_orders_date ON orders(order_date);

-- Order Items table
CREATE TABLE IF NOT EXISTS order_items (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           order_id BIGINT NOT NULL,
                                           coffee_id BIGINT NOT NULL,
                                           quantity INTEGER NOT NULL,
                                           price DECIMAL(10, 2) NOT NULL,
    CONSTRAINT order_items_quantity_positive CHECK (quantity > 0),
    CONSTRAINT order_items_price_positive CHECK (price > 0),
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_coffee FOREIGN KEY (coffee_id) REFERENCES coffee(id)
    );

CREATE INDEX IF NOT EXISTS idx_order_items_order_id ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_order_items_coffee_id ON order_items(coffee_id);