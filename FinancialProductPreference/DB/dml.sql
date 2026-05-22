BEGIN;

-- Users
INSERT INTO users (user_id, user_name, email, account) VALUES
('A123456789','alice', 'alice@example.com', '1234567890'),
('B123456789','bob', 'bob@example.com', '2345678901'),
('C123456789','charlie', 'charlie@example.com', '3456789012');

-- Products
INSERT INTO products (product_name, price, fee_rate) VALUES
('Global Tech ETF', 1200.00, 0.015),
('Asia Growth Fund', 850.00, 0.020),
('US Index Fund', 1500.00, 0.010),
('Green Energy ETF', 980.00, 0.018),
('Bond Stable Fund', 500.00, 0.005),
('Crypto Index Fund', 2000.00, 0.030),
('AI Innovation Fund', 1750.00, 0.022),
('Dividend Income Fund', 1100.00, 0.012);

COMMIT;
