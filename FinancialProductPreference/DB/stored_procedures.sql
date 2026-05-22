CREATE OR REPLACE PROCEDURE add_like_list(
    p_user_id VARCHAR(50),
    p_product_id BIGINT,
    p_quantity INT,
    p_account VARCHAR
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_price INTEGER;
    v_fee_rate FLOAT;
    v_total_amount FLOAT;
    v_total_fee FLOAT;
BEGIN

    SELECT price, fee_rate
    INTO v_price, v_fee_rate
    FROM products
    WHERE id = p_product_id;

    v_total_amount := v_price * p_quantity;
    v_total_fee := v_total_amount * COALESCE(v_fee_rate, 0);
    v_total_amount := v_total_amount + v_total_fee;

    INSERT INTO like_list (
        user_id,
        product_id,
        purchase_quantity,
        account,
        total_amount,
        total_fee
    )
    VALUES (
        p_user_id,
        p_product_id,
        p_quantity,
        p_account,
        v_total_amount,
        v_total_fee
    );

END;
$$;

CREATE OR REPLACE PROCEDURE update_like_list(
    p_sn BIGINT,
    p_product_id BIGINT,
    p_quantity INT,
    p_account VARCHAR
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_price INTEGER;
    v_fee_rate FLOAT;
    v_total_amount FLOAT;
    v_total_fee FLOAT;
BEGIN

    SELECT price, fee_rate
    INTO v_price, v_fee_rate
    FROM products
    WHERE id = p_product_id;

    v_total_amount := v_price * p_quantity;
    v_total_fee := v_total_amount * COALESCE(v_fee_rate, 0);
    v_total_amount := v_total_amount + v_total_fee;

    UPDATE like_list
    SET 
        product_id = p_product_id,
        purchase_quantity = p_quantity,
        account = p_account,
        total_amount = v_total_amount,
        total_fee = v_total_fee
    WHERE sn = p_sn;

END;
$$;