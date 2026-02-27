-- Seed inventory data for demo products (loaded after schema creation)
INSERT INTO inventory_items (product_id, product_name, available_quantity, reserved_quantity)
VALUES ('PROD-001', 'Laptop Pro 15"', 50, 0);

INSERT INTO inventory_items (product_id, product_name, available_quantity, reserved_quantity)
VALUES ('PROD-002', 'Wireless Headphones', 200, 0);

INSERT INTO inventory_items (product_id, product_name, available_quantity, reserved_quantity)
VALUES ('PROD-003', 'Mechanical Keyboard', 75, 0);

INSERT INTO inventory_items (product_id, product_name, available_quantity, reserved_quantity)
VALUES ('PROD-004', 'USB-C Hub', 150, 0);

INSERT INTO inventory_items (product_id, product_name, available_quantity, reserved_quantity)
VALUES ('PROD-005', '4K Monitor 27"', 30, 0);
