package com.smartstock.web;

import com.smartstock.order.CustomerOrder;
import com.smartstock.order.CustomerOrderRepository;
import com.smartstock.product.Product;
import com.smartstock.product.ProductRepository;
import com.smartstock.stock.StockMovementRepository;
import com.smartstock.supplier.SupplierRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ReportsController {
    private final ProductRepository products;
    private final CustomerOrderRepository orders;
    private final SupplierRepository suppliers;
    private final StockMovementRepository movements;

    public ReportsController(ProductRepository products, CustomerOrderRepository orders,
                             SupplierRepository suppliers, StockMovementRepository movements) {
        this.products = products;
        this.orders = orders;
        this.suppliers = suppliers;
        this.movements = movements;
    }

    public static class MonthOption {
        private final int number;
        private final String name;
        public MonthOption(int number, String name) { this.number = number; this.name = name; }
        public int getNumber() { return number; }
        public String getName() { return name; }
    }

    public static class ProductSales {
        private final String sku;
        private final String name;
        private final long quantity;
        private final long orderCount;
        private final String revenue;
        public ProductSales(String sku, String name, long quantity, long orderCount, BigDecimal revenue) {
            this.sku = sku; this.name = name; this.quantity = quantity; this.orderCount = orderCount;
            this.revenue = money(revenue);
        }
        public String getSku() { return sku; }
        public String getName() { return name; }
        public long getQuantity() { return quantity; }
        public long getOrderCount() { return orderCount; }
        public String getRevenue() { return revenue; }
    }

    public static class InventoryRow {
        private final String sku;
        private final String name;
        private final String category;
        private final int stock;
        private final String unitPrice;
        private final String stockValue;
        public InventoryRow(Product product) {
            sku = product.getSku(); name = product.getName(); category = product.getCategory(); stock = product.getStock();
            unitPrice = money(product.getPrice());
            stockValue = money(product.getPrice().multiply(BigDecimal.valueOf(product.getStock())));
        }
        public String getSku() { return sku; }
        public String getName() { return name; }
        public String getCategory() { return category; }
        public int getStock() { return stock; }
        public String getUnitPrice() { return unitPrice; }
        public String getStockValue() { return stockValue; }
    }

    @GetMapping("/reports")
    public String reports(@RequestParam(required = false) Integer year,
                          @RequestParam(required = false) Integer month,
                          Model model) {
        YearMonth selected = validPeriod(year, month) ? YearMonth.of(year, month) : YearMonth.now();
        List<CustomerOrder> monthOrders = orders.findByOrderDateBetweenOrderByOrderDateDesc(
                selected.atDay(1), selected.atEndOfMonth());
        List<CustomerOrder> countedOrders = monthOrders.stream()
                .filter(order -> !"CANCELLED".equalsIgnoreCase(order.getStatus())).toList();

        Map<Long, List<CustomerOrder>> productOrders = countedOrders.stream()
                .filter(order -> order.getProduct() != null)
                .collect(Collectors.groupingBy(order -> order.getProduct().getId()));

        List<ProductSales> productSales = products.findAll().stream()
                .filter(product -> productOrders.containsKey(product.getId()))
                .map(product -> {
                    List<CustomerOrder> rows = productOrders.get(product.getId());
                    BigDecimal completedRevenue = rows.stream()
                            .filter(order -> "COMPLETED".equalsIgnoreCase(order.getStatus()))
                            .map(CustomerOrder::getTotal).filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    long quantity = rows.stream().mapToLong(CustomerOrder::getQuantity).sum();
                    return new ProductSales(product.getSku(), product.getName(), quantity, rows.size(), completedRevenue);
                })
                .sorted(Comparator.comparingLong(ProductSales::getQuantity).reversed())
                .toList();

        List<Product> activeProducts = products.findAll().stream().filter(Product::isActive).toList();
        List<InventoryRow> inventoryRows = activeProducts.stream().map(InventoryRow::new).toList();
        BigDecimal stockValue = activeProducts.stream()
                .map(product -> product.getPrice().multiply(BigDecimal.valueOf(product.getStock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal monthRevenue = countedOrders.stream()
                .filter(order -> "COMPLETED".equalsIgnoreCase(order.getStatus()))
                .map(CustomerOrder::getTotal).filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<MonthOption> monthOptions = Arrays.stream(java.time.Month.values())
                .map(value -> new MonthOption(value.getValue(), value.getDisplayName(TextStyle.FULL, Locale.ENGLISH)))
                .toList();
        model.addAttribute("monthOptions", monthOptions);
        model.addAttribute("selectedMonth", selected.getMonthValue());
        model.addAttribute("selectedYear", selected.getYear());
        model.addAttribute("monthLabel", selected.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + selected.getYear());
        model.addAttribute("monthOrderCount", countedOrders.size());
        model.addAttribute("monthRevenue", money(monthRevenue));
        model.addAttribute("stockValue", money(stockValue));
        model.addAttribute("supplierCount", suppliers.count());
        model.addAttribute("movementCount", movements.count());
        model.addAttribute("productSales", productSales);
        model.addAttribute("inventoryRows", inventoryRows);
        return "reports";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        List<Product> activeProducts = products.findAll().stream().filter(Product::isActive).toList();
        model.addAttribute("activeProductCount", activeProducts.size());
        model.addAttribute("lowStockCount", activeProducts.stream().filter(Product::isLowStock).count());
        model.addAttribute("totalStock", activeProducts.stream().mapToInt(Product::getStock).sum());
        model.addAttribute("supplierCount", suppliers.count());
        return "settings";
    }

    private static boolean validPeriod(Integer year, Integer month) {
        return year != null && month != null && year >= 2000 && year <= 2100 && month >= 1 && month <= 12;
    }

    private static String money(BigDecimal value) {
        return "$" + String.format(Locale.US, "%,.2f", value == null ? BigDecimal.ZERO : value);
    }
}
