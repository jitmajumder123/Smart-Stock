package com.smartstock.product;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
@Entity
@Table(name="products", uniqueConstraints=@UniqueConstraint(columnNames="sku"))
public class Product {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotBlank @Column(nullable=false, unique=true, length=40) private String sku;
 @NotBlank @Column(nullable=false) private String name;
 @NotBlank private String category;
 @NotBlank private String supplier;
 @NotNull @DecimalMin("0.00") @Column(nullable=false, precision=12, scale=2) private BigDecimal price=BigDecimal.ZERO;
 @NotNull @Min(0) @Column(nullable=false) private Integer stock=0;
 @NotNull @Min(0) @Column(nullable=false) private Integer reorderPoint=10;
 private boolean active=true;
 public Long getId(){return id;} public void setId(Long id){this.id=id;}
 public String getSku(){return sku;} public void setSku(String sku){this.sku=sku;}
 public String getName(){return name;} public void setName(String name){this.name=name;}
 public String getCategory(){return category;} public void setCategory(String category){this.category=category;}
 public String getSupplier(){return supplier;} public void setSupplier(String supplier){this.supplier=supplier;}
 public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal price){this.price=price;}
 public Integer getStock(){return stock;} public void setStock(Integer stock){this.stock=stock;}
 public Integer getReorderPoint(){return reorderPoint;} public void setReorderPoint(Integer reorderPoint){this.reorderPoint=reorderPoint;}
 public boolean isActive(){return active;} public void setActive(boolean active){this.active=active;}
 public boolean isLowStock(){return stock != null && reorderPoint != null && stock <= reorderPoint;}
}
