package com.smartstock.config;

import com.smartstock.order.*;
import com.smartstock.product.*;
import com.smartstock.supplier.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Configuration
public class DemoData {
    @Bean @Profile("demo") CommandLineRunner seed(ProductRepository products,SupplierRepository suppliers,CustomerOrderRepository orders,OrderService orderService){
        return args->{
            if(products.count()==0){
                products.save(product("SKU-1842","Wireless Bluetooth Headphones","Electronics","Audio House","49.99",18,20));
                products.save(product("SKU-2956","Organic Cotton T-Shirt","Apparel","Green Threads","19.50",36,12));
                products.save(product("SKU-3421","Steel Water Bottle","Accessories","Hydro Goods","14.99",24,10));
                products.save(product("SKU-4100","USB-C Charging Cable","Electronics","Tech Depot","9.99",86,25));
                products.save(product("SKU-5120","Desk Lamp","Home","Bright Living","32.00",42,10));
            }
            if(suppliers.count()==0){
                suppliers.save(supplier("Audio House","Priya Shah","orders@audiohouse.example","+1 555 0101"));
                suppliers.save(supplier("Green Threads","Sam Lee","sales@greenthreads.example","+1 555 0102"));
                suppliers.save(supplier("Tech Depot","Jordan Kim","supply@techdepot.example","+1 555 0103"));
            }
            if(orders.count()==0){
                List<Product> available=products.findAll().stream().filter(p->p.isActive()&&p.getStock()>0).limit(4).toList();
                for(int i=0;i<available.size();i++){
                    OrderForm form=new OrderForm();form.setCustomerName(List.of("Northwind Retail","Harbor Market","Juniper Stores","Cedar & Co.").get(i));
                    form.setProductId(available.get(i).getId());form.setQuantity(1+i%2);form.setStatus("COMPLETED");
                    form.setOrderDate(YearMonth.now().minusMonths(3-i).atDay(5+i));orderService.save(form);
                }
            }
        };
    }
    private static Product product(String sku,String name,String category,String supplier,String price,int stock,int reorder){Product p=new Product();p.setSku(sku);p.setName(name);p.setCategory(category);p.setSupplier(supplier);p.setPrice(new BigDecimal(price));p.setStock(stock);p.setReorderPoint(reorder);return p;}
    private static Supplier supplier(String name,String contact,String email,String phone){Supplier s=new Supplier();s.setName(name);s.setContactName(contact);s.setEmail(email);s.setPhone(phone);return s;}
}
