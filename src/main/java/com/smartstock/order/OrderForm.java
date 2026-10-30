package com.smartstock.order;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class OrderForm {
    private Long id;
    @NotBlank private String customerName;
    @NotNull @Positive private Long productId;
    @NotNull @Min(1) private Integer quantity=1;
    @NotBlank @Pattern(regexp="PENDING|PROCESSING|COMPLETED|CANCELLED") private String status="PENDING";
    @NotNull private LocalDate orderDate=LocalDate.now();
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getCustomerName(){return customerName;} public void setCustomerName(String v){customerName=v;}
    public Long getProductId(){return productId;} public void setProductId(Long v){productId=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public LocalDate getOrderDate(){return orderDate;} public void setOrderDate(LocalDate v){orderDate=v;}
}
