package com.smartstock.product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface ProductRepository extends JpaRepository<Product,Long> {
 boolean existsBySkuIgnoreCase(String sku);
 @Query("select p from Product p where (:q is null or lower(p.name) like lower(concat('%',:q,'%')) or lower(p.sku) like lower(concat('%',:q,'%'))) order by p.name")
 java.util.List<Product> search(@Param("q") String q);
 long countByActiveTrue();
 long countByStockLessThanEqualAndActiveTrue(Integer stock);
}
