package com.smartstock.web;

import com.smartstock.supplier.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {
    private final SupplierRepository suppliers;
    public SupplierController(SupplierRepository suppliers){this.suppliers=suppliers;}
    @GetMapping public String list(Model model){model.addAttribute("suppliers",suppliers.findAllByOrderByNameAsc());return "suppliers";}
    @GetMapping("/new") public String create(Model model){model.addAttribute("supplier",new Supplier());return "supplier-form";}
    @GetMapping("/edit/{id}") public String edit(@PathVariable Long id,Model model){model.addAttribute("supplier",suppliers.findById(id).orElseThrow());return "supplier-form";}
    @PostMapping("/save") public String save(@Valid @ModelAttribute("supplier") Supplier supplier,BindingResult result){
        if(supplier.getName()!=null && suppliers.existsByNameIgnoreCase(supplier.getName().trim())){
            boolean same=supplier.getId()!=null && suppliers.findById(supplier.getId()).map(s->s.getName().equalsIgnoreCase(supplier.getName().trim())).orElse(false);
            if(!same)result.rejectValue("name","duplicate","A supplier with this name already exists.");
        }
        if(result.hasErrors())return "supplier-form";
        supplier.setName(supplier.getName().trim());suppliers.save(supplier);return "redirect:/suppliers";
    }
    @PostMapping("/delete/{id}") public String delete(@PathVariable Long id,RedirectAttributes ra){suppliers.deleteById(id);ra.addFlashAttribute("success","Supplier deleted.");return "redirect:/suppliers";}
}
