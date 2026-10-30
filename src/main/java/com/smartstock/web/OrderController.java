package com.smartstock.web;

import com.smartstock.order.*;
import com.smartstock.product.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final CustomerOrderRepository orders;
    private final OrderService service;
    private final ProductRepository products;
    public OrderController(CustomerOrderRepository orders,OrderService service,ProductRepository products){this.orders=orders;this.service=service;this.products=products;}
    @GetMapping public String list(Model model){model.addAttribute("orders",orders.findAllByOrderByOrderDateDesc());return "orders";}
    @GetMapping("/new") public String create(Model model){model.addAttribute("orderForm",new OrderForm());loadProducts(model);return "order-form";}
    @GetMapping("/edit/{id}") public String edit(@PathVariable Long id,Model model){
        var order=service.find(id); var form=new OrderForm(); form.setId(order.getId());form.setCustomerName(order.getCustomerName());form.setQuantity(order.getQuantity());form.setStatus(order.getStatus());form.setOrderDate(order.getOrderDate());
        if(order.getProduct()!=null) form.setProductId(order.getProduct().getId());
        model.addAttribute("orderForm",form);loadProducts(model);return "order-form";
    }
    @PostMapping("/save") public String save(@Valid @ModelAttribute("orderForm") OrderForm form,BindingResult result,Model model,RedirectAttributes ra){
        if(result.hasErrors()){loadProducts(model);return "order-form";}
        try{service.save(form);ra.addFlashAttribute("success","Order saved. Stock was updated automatically.");return "redirect:/orders";}
        catch(IllegalArgumentException ex){model.addAttribute("error",ex.getMessage());loadProducts(model);return "order-form";}
    }
    @PostMapping("/delete/{id}") public String delete(@PathVariable Long id,RedirectAttributes ra){service.delete(id);ra.addFlashAttribute("success","Order deleted and reserved stock returned.");return "redirect:/orders";}
    private void loadProducts(Model model){model.addAttribute("products",products.findAll().stream().filter(p->p.isActive()).toList());}
}
