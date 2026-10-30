package com.smartstock.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="customer_orders")
public class CustomerOrder {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank @Column(nullable=false,length=120) private String customerName;
    // item remains as a snapshot so existing installations with the legacy NOT NULL column stay compatible.
    @NotBlank @Column(nullable=false,length=180) private String item;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="product_id") private com.smartstock.product.Product product;
    @NotNull @Min(1) @Column(nullable=false) private Integer quantity=1;
    @NotNull @DecimalMin("0.00") @Column(nullable=false,precision=12,scale=2) private BigDecimal total=BigDecimal.ZERO;
    @NotBlank @Pattern(regexp="PENDING|PROCESSING|COMPLETED|CANCELLED") @Column(nullable=false,length=24) private String status="PENDING";
    @NotNull @Column(nullable=false) private LocalDate orderDate=LocalDate.now();
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getCustomerName(){return customerName;} public void setCustomerName(String v){customerName=v;}
    public String getItem(){return item;} public void setItem(String v){item=v;}
    public com.smartstock.product.Product getProduct(){return product;} public void setProduct(com.smartstock.product.Product v){product=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
    public BigDecimal getTotal(){return total;} public void setTotal(BigDecimal v){total=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public LocalDate getOrderDate(){return orderDate;} public void setOrderDate(LocalDate v){orderDate=v;}
}
