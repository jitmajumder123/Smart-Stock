package com.smartstock.order;

import com.smartstock.product.Product;
import com.smartstock.product.ProductRepository;
import com.smartstock.product.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class OrderService {
    private final CustomerOrderRepository orders;
    private final ProductRepository products;
    private final ProductService productService;
    public OrderService(CustomerOrderRepository orders,ProductRepository products,ProductService productService){this.orders=orders;this.products=products;this.productService=productService;}

    @Transactional
    public CustomerOrder save(OrderForm form){
        CustomerOrder order=form.getId()==null?new CustomerOrder():orders.findWithProductById(form.getId()).orElseThrow();
        Product selected=products.findById(form.getProductId()).orElseThrow(()->new IllegalArgumentException("Choose an existing product."));
        if(!selected.isActive()) throw new IllegalArgumentException("Inactive products cannot be ordered.");
        Product oldProduct=order.getProduct();
        int oldQuantity=order.getQuantity()==null?0:order.getQuantity();
        boolean wasReserved=order.getId()!=null && oldProduct!=null && !"CANCELLED".equals(order.getStatus());
        boolean willReserve=!"CANCELLED".equals(form.getStatus());
        boolean changed=oldProduct==null || !oldProduct.getId().equals(selected.getId()) || oldQuantity!=form.getQuantity();
        if(wasReserved && (changed || !willReserve)) productService.adjustStock(oldProduct.getId(),oldQuantity,"Order #"+order.getId()+" returned to stock");
        if(willReserve && (changed || !wasReserved)) productService.adjustStock(selected.getId(),-form.getQuantity(),"Order reservation");
        order.setCustomerName(form.getCustomerName().trim());
        order.setProduct(selected);
        order.setItem(selected.getName());
        order.setQuantity(form.getQuantity());
        order.setTotal(order.getId()!=null && !changed && order.getTotal()!=null ? order.getTotal() : selected.getPrice().multiply(BigDecimal.valueOf(form.getQuantity())));
        order.setStatus(form.getStatus());
        order.setOrderDate(form.getOrderDate());
        return orders.save(order);
    }
    @Transactional
    public void delete(Long id){
        CustomerOrder order=orders.findById(id).orElseThrow();
        if(!"CANCELLED".equals(order.getStatus()) && order.getProduct()!=null)
            productService.adjustStock(order.getProduct().getId(),order.getQuantity(),"Order #"+id+" deleted; stock returned");
        orders.delete(order);
    }
    public CustomerOrder find(Long id){return orders.findWithProductById(id).orElseThrow();}
}
