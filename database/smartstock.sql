CREATE DATABASE IF NOT EXISTS smartstock CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE smartstock;

-- Spring Boot also creates/updates these tables from the JPA entities on startup.
CREATE TABLE IF NOT EXISTS products (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  sku VARCHAR(40) NOT NULL UNIQUE,
  name VARCHAR(255) NOT NULL,
  category VARCHAR(255) NOT NULL,
  supplier VARCHAR(255) NOT NULL,
  price DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  stock INT NOT NULL DEFAULT 0,
  reorder_point INT NOT NULL DEFAULT 10,
  active BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE IF NOT EXISTS stock_movement (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  product_id BIGINT,
  sku VARCHAR(255),
  product_name VARCHAR(255),
  movement_type VARCHAR(255),
  quantity INT NOT NULL,
  balance_after INT NOT NULL,
  note VARCHAR(255),
  created_at DATETIME(6)
);
CREATE TABLE IF NOT EXISTS suppliers (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(120) NOT NULL UNIQUE,
  contact_name VARCHAR(80) NOT NULL,
  email VARCHAR(160),
  phone VARCHAR(30),
  address VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS customer_orders (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  customer_name VARCHAR(120) NOT NULL,
  item VARCHAR(180) NOT NULL,
  product_id BIGINT NULL,
  quantity INT NOT NULL,
  total DECIMAL(12,2) NOT NULL,
  status VARCHAR(24) NOT NULL,
  order_date DATE NOT NULL,
  CONSTRAINT fk_customer_orders_product FOREIGN KEY (product_id) REFERENCES products(id)
);
