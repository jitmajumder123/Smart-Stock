package com.smartstock.stock;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class StockMovement {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private Long productId; private String sku; private String productName;
 private String movementType; private int quantity; private int balanceAfter; private String note;
 private LocalDateTime createdAt=LocalDateTime.now();
 public StockMovement() {}
 public StockMovement(Long productId,String sku,String productName,String movementType,int quantity,int balanceAfter,String note){
 this.productId=productId;this.sku=sku;this.productName=productName;this.movementType=movementType;this.quantity=quantity;this.balanceAfter=balanceAfter;this.note=note;this.createdAt=LocalDateTime.now();}
 public Long getId(){return id;} public Long getProductId(){return productId;} public String getSku(){return sku;}
 public String getProductName(){return productName;} public String getMovementType(){return movementType;}
 public int getQuantity(){return quantity;} public int getBalanceAfter(){return balanceAfter;} public String getNote(){return note;} public LocalDateTime getCreatedAt(){return createdAt;}
}
