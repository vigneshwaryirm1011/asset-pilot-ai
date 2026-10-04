package com.pro.assetpilot.service;

import com.pro.assetpilot.dto.ApiModel;
import com.pro.assetpilot.exception.BusinessException;
import com.pro.assetpilot.model.Product;
import com.pro.assetpilot.model.PurchaseOrder;
import com.pro.assetpilot.repository.ProductRepository;
import com.pro.assetpilot.repository.PurchaseOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;

public class OrderService {
    private final ProductRepository productRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public OrderService(ProductRepository productRepository, PurchaseOrderRepository purchaseOrderRepository) {
        this.productRepository = productRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @Transactional
    public ApiModel.OrderView place(String owner, String key, ApiModel.PlaceOrder request) {
        if (key == null || !key.matches("[A-Za-z0-9._-]{1,80}")) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Idempotency-Key must be 1-80 letters, digits, dots, underscores or hyphens");
        }
        var previous = purchaseOrderRepository.findByOwnerAndRequestKey(owner, key);

        if (previous.isPresent()) return replay(previous.get(), request);
        Product product = productRepository.findForUpdate(request.productId()).orElseThrow(() ->
                new BusinessException(HttpStatus.NOT_FOUND, "Product not found"));

        previous = purchaseOrderRepository.findByOwnerAndRequestKey(owner, key);

        if (previous.isPresent()) return replay(previous.get(), request);
        product.reserve(request.quantity());

        return purchaseOrderRepository.saveAndFlush(new PurchaseOrder(owner, key, product, request.quantity())).view();
    }


    private ApiModel.OrderView replay(PurchaseOrder order, ApiModel.PlaceOrder request) {
        if (!order.matches(request))
            throw new BusinessException(HttpStatus.CONFLICT, "Idempotency-Key already used for a different order");
        return order.view();
    }
    @Transactional
    public ApiModel.OrderView get(String owner, Long id) {
        return purchaseOrderRepository.findByIdAndOwner(id, owner).orElseThrow(() ->
                new BusinessException(HttpStatus.NOT_FOUND, "Order not found")).view();
    }


}
