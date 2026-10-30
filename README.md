# SmartStock — Inventory & Order Management

Java 23 full-stack project using Spring Boot, Spring MVC, Spring Data JPA/Hibernate, MySQL, Thymeleaf, HTML, CSS, Spring Security, and Maven. The UI uses no JavaScript.

## Working modules

- **Products:** create, search, edit, and deactivate catalogue products.
- **Inventory:** view stock/reorder levels, adjust stock with movement audit history, and review order reservations.
- **Orders:** create/edit product-linked orders, automatically calculate totals, reserve stock, and return stock when cancelled or deleted.
- **Suppliers:** create, list, and edit supplier contacts; admins can delete suppliers.
- **Reports:** select a month/year and see units ordered, order counts, completed revenue by product, and current inventory valuation.
- **Dashboard:** active product/stock counts, non-cancelled order count, completed-order revenue, monthly revenue bars, stock by category, alerts, and movement history.

## Role permissions

| Role | Allowed work |
| --- | --- |
| ADMIN | All modules and actions, including deleting orders/suppliers and settings. |
| MANAGER | Product and inventory maintenance, supplier create/edit, order create/edit, dashboard and reports. Cannot access settings or delete orders/suppliers. |
| SALES | Read products/inventory, create/edit orders, dashboard and reports. Cannot directly adjust stock or maintain suppliers. |

Demo accounts: `admin / Admin@123`, `manager / Manager@123`, `sales / Sales@123`. These are in-memory development accounts, not production user storage.

## See working sample data

Use JDK 23 and Maven 3.9+, from the folder containing `pom.xml`:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

The demo profile uses H2 and seeds example products, suppliers, and completed orders across recent months. This makes the dashboard totals and monthly product report non-zero immediately.

## Use MySQL

1. Run `database/smartstock.sql` in MySQL, or allow the configured JDBC URL to create the database.
2. Set `DB_USERNAME` and `DB_PASSWORD`; optionally set `DB_URL` (defaults to `jdbc:mysql://localhost:3306/smartstock`).
3. Run `mvn spring-boot:run` and open http://localhost:8080.
4. Add products and suppliers, then create orders using the product selector. Set an order to **Completed** for it to count as revenue.

A new MySQL database starts empty, so its totals correctly show zero until records are entered. To populate a fresh MySQL database with example records, run `database/sample-data.sql` once after the schema. The Reports page has month/year filters and shows the quantity and number of orders for each product in that period. Legacy free-text orders need to be edited and linked to a product to appear in that per-product breakdown.

## Notes

- Product-linked orders reserve stock while Pending/Processing/Completed. Cancelling or deleting returns stock and records the movement.
- Revenue counts Completed orders only. Order counts exclude Cancelled orders.
- `spring.jpa.hibernate.ddl-auto=update` applies the new nullable `product_id` field to existing MySQL databases. Back up production data before schema changes.
- Do not use demo credentials or commit real database passwords in production.
