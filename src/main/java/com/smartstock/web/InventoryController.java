package com.smartstock.web;

import com.smartstock.product.ProductService;
import com.smartstock.stock.StockMovementRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InventoryController {
    private final ProductService products;
    private final StockMovementRepository movements;
    public InventoryController(ProductService products,StockMovementRepository movements){this.products=products;this.movements=movements;}
    @GetMapping("/inventory") public String inventory(@RequestParam(required=false) String q,Model model){
        model.addAttribute("products",products.findAll(q));
        model.addAttribute("q",q==null?"":q);
        model.addAttribute("movements",movements.findTop50ByOrderByCreatedAtDesc());
        return "inventory";
    }
}
