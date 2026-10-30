package com.smartstock.web;
import com.smartstock.product.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequestMapping("/products")
public class ProductController {
 private final ProductService service;
 public ProductController(ProductService service){this.service=service;}
 @GetMapping public String list(@RequestParam(required=false) String q,Model model){model.addAttribute("products",service.findAll(q));model.addAttribute("q",q==null?"":q);return "products";}
 @GetMapping("/new") public String create(Model model){model.addAttribute("product",new Product());return "product-form";}
 @GetMapping("/edit/{id}") public String edit(@PathVariable Long id,Model model){model.addAttribute("product",service.find(id));return "product-form";}
 @PostMapping("/save") public String save(@Valid @ModelAttribute Product product,BindingResult result,RedirectAttributes ra){
   if(result.hasErrors()) return "product-form";
   try {service.save(product);ra.addFlashAttribute("success","Product saved successfully.");return "redirect:/products";}
   catch(IllegalArgumentException ex){result.rejectValue("sku","duplicate",ex.getMessage());return "product-form";}
 }
 @PostMapping("/delete/{id}") public String delete(@PathVariable Long id,RedirectAttributes ra){service.delete(id);ra.addFlashAttribute("success","Product deactivated.");return "redirect:/products";}
 @GetMapping("/stock/{id}") public String stockForm(@PathVariable Long id,Model model){model.addAttribute("product",service.find(id));return "stock-form";}
 @PostMapping("/stock/{id}") public String adjust(@PathVariable Long id,@RequestParam int delta,@RequestParam(defaultValue="Manual adjustment") String note,RedirectAttributes ra){
   try{service.adjustStock(id,delta,note);ra.addFlashAttribute("success","Stock updated.");}
   catch(IllegalArgumentException ex){ra.addFlashAttribute("error",ex.getMessage());}
   return "redirect:/products";
 }
}
