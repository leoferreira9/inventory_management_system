package com.leo.inventory_management_system.repository;

import com.leo.inventory_management_system.entity.StockMovement;
import com.leo.inventory_management_system.enums.MovementReason;
import com.leo.inventory_management_system.enums.MovementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    @Query("""
            SELECT sm FROM StockMovement sm
            WHERE (:productId IS NULL OR sm.product.id = :productId)
            AND (:productName IS NULL OR sm.product.name LIKE %:productName%)
            AND (:type IS NULL OR sm.type = :type)
            AND (:reason IS NULL OR sm.reason = :reason)
            AND (:startDate IS NULL OR sm.occurredAt >= :startDate)
            AND (:endDate IS NULL OR sm.occurredAt <= :endDate)
            """)
    Page<StockMovement> findWithFilters(Long productId,
                                        String productName,
                                        MovementType type,
                                        MovementReason reason,
                                        LocalDateTime startDate,
                                        LocalDateTime endDate,
                                        Pageable pageable);
}
