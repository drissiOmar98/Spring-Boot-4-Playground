-- Sample coffee products for the coffee shop demo

-- Clear existing data to allow script to run multiple times
-- Delete in order to respect foreign key constraints (H2 compatible)
DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM coffee;

-- Reset auto-increment sequences
ALTER TABLE order_items ALTER COLUMN id RESTART WITH 1;
ALTER TABLE orders ALTER COLUMN id RESTART WITH 1;
ALTER TABLE coffee ALTER COLUMN id RESTART WITH 1;

-- Small coffees
INSERT INTO coffee (name, description, price, size) VALUES
                                                        ('Espresso', 'Rich and bold single shot of espresso', 2.50, 'SMALL'),
                                                        ('Americano', 'Espresso with hot water', 3.00, 'SMALL'),
                                                        ('Macchiato', 'Espresso with a dollop of foam', 3.25, 'SMALL');

-- Medium coffees
INSERT INTO coffee (name, description, price, size) VALUES
                                                        ('Cappuccino', 'Espresso with steamed milk and foam', 4.50, 'MEDIUM'),
                                                        ('Latte', 'Espresso with steamed milk', 4.75, 'MEDIUM'),
                                                        ('Flat White', 'Espresso with velvety microfoam', 4.50, 'MEDIUM'),
                                                        ('Mocha', 'Espresso with chocolate and steamed milk', 5.25, 'MEDIUM'),
                                                        ('Caramel Macchiato', 'Vanilla and espresso with caramel drizzle', 5.50, 'MEDIUM');

-- Large coffees
INSERT INTO coffee (name, description, price, size) VALUES
                                                        ('Large Latte', 'Double shot espresso with extra steamed milk', 5.75, 'LARGE'),
                                                        ('Large Cappuccino', 'Double shot espresso with steamed milk and foam', 5.50, 'LARGE'),
                                                        ('Large Mocha', 'Double shot espresso with chocolate and steamed milk', 6.25, 'LARGE'),
                                                        ('Caffe Misto', 'Brewed coffee with steamed milk', 4.50, 'LARGE'),
                                                        ('Cold Brew', 'Smooth cold-steeped coffee', 5.00, 'LARGE'),
                                                        ('Iced Latte', 'Espresso with cold milk over ice', 5.50, 'LARGE'),
                                                        ('Vanilla Latte', 'Espresso with vanilla and steamed milk', 6.00, 'LARGE');

-- Sample orders to demonstrate AOT query capabilities (H2 compatible timestamps)
INSERT INTO orders (customer_id, customer_name, order_date, total_amount, status) VALUES
                                                                                      (1, 'Alice Johnson', DATEADD('DAY', -2, CURRENT_TIMESTAMP), 8.25, 'DELIVERED'),
                                                                                      (2, 'Bob Smith', DATEADD('DAY', -1, CURRENT_TIMESTAMP), 12.50, 'DELIVERED'),
                                                                                      (1, 'Alice Johnson', DATEADD('HOUR', -5, CURRENT_TIMESTAMP), 5.75, 'READY'),
                                                                                      (3, 'Charlie Brown', DATEADD('HOUR', -2, CURRENT_TIMESTAMP), 15.00, 'PREPARING'),
                                                                                      (4, 'Diana Prince', DATEADD('MINUTE', -30, CURRENT_TIMESTAMP), 4.50, 'PENDING'),
                                                                                      (2, 'Bob Smith', DATEADD('MINUTE', -10, CURRENT_TIMESTAMP), 9.00, 'PENDING');

-- Sample order items to demonstrate relationships and JOINs
-- Alice's first order (2 days ago)
INSERT INTO order_items (order_id, coffee_id, quantity, price) VALUES
                                                                   (1, 4, 1, 4.50),  -- Cappuccino
                                                                   (1, 2, 1, 3.00),  -- Americano
                                                                   (1, 5, 1, 4.75);  -- Latte

-- Bob's first order (1 day ago)
INSERT INTO order_items (order_id, coffee_id, quantity, price) VALUES
                                                                   (2, 9, 2, 5.75),  -- 2x Large Latte
                                                                   (2, 1, 1, 2.50);  -- Espresso

-- Alice's recent order (5 hours ago)
INSERT INTO order_items (order_id, coffee_id, quantity, price) VALUES
    (3, 9, 1, 5.75);  -- Large Latte

-- Charlie's preparing order (2 hours ago)
INSERT INTO order_items (order_id, coffee_id, quantity, price) VALUES
                                                                   (4, 11, 1, 6.25),  -- Large Mocha
                                                                   (4, 7, 1, 5.25),   -- Mocha
                                                                   (4, 2, 1, 3.00);   -- Americano

-- Diana's pending order (30 minutes ago)
INSERT INTO order_items (order_id, coffee_id, quantity, price) VALUES
    (5, 4, 1, 4.50);  -- Cappuccino

-- Bob's recent pending order (10 minutes ago)
INSERT INTO order_items (order_id, coffee_id, quantity, price) VALUES
                                                                   (6, 7, 1, 5.25),  -- Mocha
                                                                   (6, 2, 1, 3.00);  -- Americano