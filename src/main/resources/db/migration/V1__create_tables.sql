create table products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(150),
    price DECIMAL(9, 2) NOT NULL,
    sku VARCHAR(40) UNIQUE NOT NULL,
    active BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME
);

create table stock_lots (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    batch_code VARCHAR(100) NOT NULL UNIQUE,
    expiry_date DATE NOT NULL,
    quantity INT NOT NULL CHECK(quantity >= 0),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE INDEX idx_stock_lots_product_expiry ON stock_lots(product_id, expiry_date);

create table stock_movements (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    type ENUM("IN", "OUT", "ADJUST") NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL CHECK(quantity > 0),
    occurred_at DATETIME,
    reason VARCHAR(150),
    reference VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

create table stock_movement_lots (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    movement_id BIGINT NOT NULL,
    lot_id BIGINT NOT NULL,
    quantity INT NOT NULL CHECK(quantity > 0),
    FOREIGN KEY (movement_id) REFERENCES stock_movements(id),
    FOREIGN KEY (lot_id) REFERENCES stock_lots(id)
);