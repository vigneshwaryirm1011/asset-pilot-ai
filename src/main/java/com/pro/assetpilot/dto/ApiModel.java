package com.pro.assetpilot.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public final class ApiModel {
    private ApiModel(){};
    //createproduct
    public record CreateProduct(
           @NotBlank @Pattern(regexp = "[A-Z0-9-]{1,40}") String sku,
           @NotBlank @Size(max=42) String name,
           @NotNull @DecimalMin("0.01") @Digits(integer = 10,fraction = 10) BigDecimal price,
           @Min(0) @Max(1000000) int stock) {}


    //placeorder
    public record PlaceOrder(
            @NotNull @Positive Long productId,
            @Min(1) @Max(1000) int quantity) {}
    //productview
    public record ProductView(Long id, String sku, String name, BigDecimal price, int stock) {}
    //orderview
    public record ProductPage(List<ProductView> items, int page, int size, long total) {}
    //productpage
    public record OrderView(Long id, Long productId, int quantity, BigDecimal unitPrice, BigDecimal total, Instant createdAt) {}
}
