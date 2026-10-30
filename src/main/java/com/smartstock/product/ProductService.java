package com.smartstock.product;
import com.smartstock.stock.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class ProductService {
 private final ProductRepository products; private final StockMovementRepository movements;
 public ProductService(ProductRepository products, StockMovementRepository movements){this.products=products;this.movements=movements;}
 public List<Product> findAll(String q){return products.search(q==null||q.isBlank()?null:q.trim()).stream().filter(Product::isActive).toList();}
 public Product find(Long id){return products.findById(id).orElseThrow(()->new IllegalArgumentException("Product not found"));}
 @Transactional public Product save(Product p){
   if(p.getId()==null && products.existsBySkuIgnoreCase(p.getSku())) throw new IllegalArgumentException("SKU already exists.");
   if(p.getId()!=null){Product old=find(p.getId()); if(!old.getSku().equalsIgnoreCase(p.getSku()) && products.existsBySkuIgnoreCase(p.getSku())) throw new IllegalArgumentException("SKU already exists."); p.setStock(old.getStock());}
   return products.save(p);
 }
 @Transactional public void delete(Long id){Product p=find(id);p.setActive(false);products.save(p);}
 @Transactional public void adjustStock(Long id,int delta,String note){
   Product p=find(id); int next=p.getStock()+delta;
   if(next<0) throw new IllegalArgumentException("Stock cannot become negative.");
   p.setStock(next); products.save(p);
   movements.save(new StockMovement(p.getId(),p.getSku(),p.getName(),delta>=0?"STOCK_IN":"STOCK_OUT",Math.abs(delta),next,note));
 }
 public long totalActive(){return products.countByActiveTrue();}
 public long lowStockCount(){return products.findAll().stream().filter(p->p.isActive()&&p.isLowStock()).count();}
 public List<StockMovement> recentMovements(){return movements.findTop20ByOrderByCreatedAtDesc();}
}
