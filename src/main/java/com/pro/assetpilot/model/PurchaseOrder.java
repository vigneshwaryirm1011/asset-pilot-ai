package com.pro.assetpilot.model;

import com.pro.assetpilot.dto.ApiModel;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table( name = "purchase_orders",
        uniqueConstraints = @UniqueConstraint(name = "uk_order_request",columnNames = {"owner", "request_key"}))
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String owner;
    @Column(name = "request_key", nullable = false, length = 80)
    private String requestKey;
    @Column(name = "product_id", nullable = false)
    private Long productId;
    @Column(nullable = false)
    private int quantity;
    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal total;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected PurchaseOrder() {}
    public PurchaseOrder(String owner, String key, Product product, int quantity) {
        this.owner = owner; this.requestKey = key; this.productId = product.getId();
        this.quantity = quantity; this.unitPrice = product.getPrice();
        this.total = unitPrice.multiply(BigDecimal.valueOf(quantity));
        this.createdAt = Instant.now();
    }
    public boolean matches(ApiModel.PlaceOrder request) {
        return productId.equals(request.productId()) && quantity == request.quantity();
    }
    public ApiModel.OrderView view() {
        return new ApiModel.OrderView(id, productId, quantity, unitPrice, total, createdAt);
    }
}
