package com.pro.assetpilot.repository;

import com.pro.assetpilot.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Long> {
    Optional<PurchaseOrder> findByOwnerAndRequestKey(String owner, String requestKey);
    Optional<PurchaseOrder> findByIdAndOwner(Long id, String owner);
}
