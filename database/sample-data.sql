-- Optional demonstration data for an empty MySQL database. Run once after smartstock.sql.
USE smartstock;
INSERT INTO products (sku,name,category,supplier,price,stock,reorder_point,active) VALUES
('DEMO-1001','Wireless Headphones','Electronics','Audio House',49.99,18,8,TRUE),
('DEMO-1002','Cotton T-Shirt','Apparel','Green Threads',19.50,36,10,TRUE),
('DEMO-1003','Steel Water Bottle','Accessories','Hydro Goods',14.99,24,8,TRUE),
('DEMO-1004','USB-C Cable','Electronics','Tech Depot',9.99,86,20,TRUE);
INSERT INTO suppliers (name,contact_name,email,phone) VALUES
('Audio House','Priya Shah','orders@audiohouse.example','+1 555 0101'),
('Green Threads','Sam Lee','sales@greenthreads.example','+1 555 0102'),
('Tech Depot','Jordan Kim','supply@techdepot.example','+1 555 0103');
INSERT INTO customer_orders (customer_name,item,product_id,quantity,total,status,order_date)
SELECT 'Northwind Retail',p.name,p.id,2,p.price*2,'COMPLETED',DATE_SUB(CURDATE(),INTERVAL 2 MONTH) FROM products p WHERE p.sku='DEMO-1001';
INSERT INTO customer_orders (customer_name,item,product_id,quantity,total,status,order_date)
SELECT 'Harbor Market',p.name,p.id,3,p.price*3,'COMPLETED',DATE_SUB(CURDATE(),INTERVAL 1 MONTH) FROM products p WHERE p.sku='DEMO-1002';
INSERT INTO customer_orders (customer_name,item,product_id,quantity,total,status,order_date)
SELECT 'Cedar & Co.',p.name,p.id,1,p.price,'COMPLETED',CURDATE() FROM products p WHERE p.sku='DEMO-1003';
UPDATE products SET stock=stock-2 WHERE sku='DEMO-1001';
UPDATE products SET stock=stock-3 WHERE sku='DEMO-1002';
UPDATE products SET stock=stock-1 WHERE sku='DEMO-1003';

INSERT INTO stock_movement (product_id,sku,product_name,movement_type,quantity,balance_after,note,created_at)
SELECT p.id,p.sku,p.name,'STOCK_OUT',o.quantity,p.stock,CONCAT('Order #',o.id,' reservation'),CURRENT_TIMESTAMP
FROM customer_orders o JOIN products p ON p.id=o.product_id WHERE p.sku IN ('DEMO-1001','DEMO-1002','DEMO-1003');
