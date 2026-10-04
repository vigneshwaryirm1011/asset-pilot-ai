package com.pro.assetpilot.model;

import com.pro.assetpilot.dto.ApiModel;
import com.pro.assetpilot.exception.BusinessException;
import jakarta.persistence.*;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

@Entity
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String sku;

    @Column(nullable = false,length = 120)
    private String name;

    @Column(nullable = false,precision = 2, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int stock;

    protected Product() {
    }
    public Product(String sku, String name, BigDecimal price, int stock) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public void reserve(int quantity){
        if(quantity <=0 ) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Quantity must be above 0");
        }
        if(stock < quantity) {
            throw new BusinessException(HttpStatus.CONFLICT,"Insufficient stock");
        }
        stock = stock - quantity;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ApiModel.ProductView view(){
        return new ApiModel.ProductView(id,sku,name,price,stock);
    }
}
