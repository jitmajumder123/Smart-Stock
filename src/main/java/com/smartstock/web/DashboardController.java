package com.smartstock.web;

import com.smartstock.product.*;
import com.smartstock.order.CustomerOrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    private final ProductService service;
    private final ProductRepository repository;
    private final CustomerOrderRepository orders;
    public DashboardController(ProductService service, ProductRepository repository, CustomerOrderRepository orders){this.service=service;this.repository=repository;this.orders=orders;}

    public static class RevenueBar {
        private final String month; private final String label; private final int height;
        public RevenueBar(String month,String label,int height){this.month=month;this.label=label;this.height=height;}
        public String getMonth(){return month;} public String getLabel(){return label;} public int getHeight(){return height;}
    }

    @GetMapping("/") public String dashboard(Model model,Authentication authentication){
        var all=repository.findAll().stream().filter(Product::isActive).toList();
        var allOrders=orders.findAll();
        var completed=allOrders.stream().filter(o->"COMPLETED".equalsIgnoreCase(o.getStatus())).toList();
        var revenue=completed.stream().map(o->o.getTotal()).reduce(BigDecimal.ZERO,BigDecimal::add);
        var current=YearMonth.now();
        var monthly=new ArrayList<BigDecimal>();
        for(int i=11;i>=0;i--){var month=current.minusMonths(i);monthly.add(completed.stream().filter(o->YearMonth.from(o.getOrderDate()).equals(month)).map(o->o.getTotal()).reduce(BigDecimal.ZERO,BigDecimal::add));}
        var max=monthly.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        var bars=new ArrayList<RevenueBar>();
        for(int i=0;i<12;i++){int h=max.signum()==0?0:monthly.get(i).multiply(BigDecimal.valueOf(100)).divide(max,0,java.math.RoundingMode.HALF_UP).intValue();bars.add(new RevenueBar(current.minusMonths(11-i).getMonth().getDisplayName(TextStyle.SHORT,Locale.ENGLISH),monthly.get(i).setScale(2,java.math.RoundingMode.HALF_UP).toPlainString(),h));}
        var categories=all.stream().collect(Collectors.groupingBy(p->p.getCategory()==null||p.getCategory().isBlank()?"Uncategorized":p.getCategory(),TreeMap::new,Collectors.summingInt(Product::getStock)));
        model.addAttribute("username",authentication.getName());
        model.addAttribute("roleLabel",authentication.getAuthorities().stream().map(a->a.getAuthority().replace("ROLE_", "")).collect(Collectors.joining(", ")));
        model.addAttribute("totalProducts",service.totalActive());
        model.addAttribute("lowStockCount",service.lowStockCount());
        model.addAttribute("totalStock",all.stream().mapToInt(Product::getStock).sum());
        model.addAttribute("lowProducts",all.stream().filter(Product::isLowStock).limit(8).toList());
        model.addAttribute("recentMovements",service.recentMovements());
        model.addAttribute("totalOrders",allOrders.stream().filter(o->!"CANCELLED".equalsIgnoreCase(o.getStatus())).count());
        model.addAttribute("revenue",revenue);
        model.addAttribute("monthlyRevenue",bars);
        model.addAttribute("categoryStock",categories);
        return "dashboard";
    }
    @GetMapping("/login") public String login(){return "login";}
    @GetMapping("/access-denied") public String accessDenied(){return "access-denied";}
}
