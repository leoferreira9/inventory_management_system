package com.leo.inventory_management_system.repository;

import com.leo.inventory_management_system.entity.StockLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockLotRepository extends JpaRepository<StockLot, Long> {
    List<StockLot> findAllByProductId(Long productId);

    @Query("SELECT SUM(sl.quantity) FROM StockLot sl WHERE sl.product.id = :productId")
    Integer sumProductQuantityStock(@Param("productId") Long productId);
}
