CREATE TABLE users(
    user_id VARCHAR(50) PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    account VARCHAR(50) NOT NULL
);

CREATE TABLE products(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    price INTEGER NOT NULL,
    fee_rate FLOAT
);

CREATE TABLE like_list(
    sn BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id VARCHAR(50) REFERENCES users(user_id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES products(id) ON DELETE CASCADE,
    purchase_quantity INTEGER NOT NULL,
    account VARCHAR(50) NOT NULL,
    total_fee FLOAT NOT NULL,
    total_amount FLOAT NOT NULL
);