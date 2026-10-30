# SmartStock — Inventory & Order Management

Java full-stack project using Spring Boot, Spring MVC, Spring Data JPA/Hibernate, MySQL, Thymeleaf, HTML, CSS, Spring Security, and Maven.

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

Role Based User: `sales / Sales@123`, `admin`, `manager`. 

## See working sample data

Use JDK 23 and Maven 3.9+, from the folder containing `pom.xml`:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

## Notes

- Product-linked orders reserve stock while Pending/Processing/Completed. Cancelling or deleting returns stock and records the movement.
- Revenue counts Completed orders only. Order counts exclude Cancelled orders.
