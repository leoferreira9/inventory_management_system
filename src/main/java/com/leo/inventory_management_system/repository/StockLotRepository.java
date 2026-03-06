package com.leo.inventory_management_system.repository;

import com.leo.inventory_management_system.entity.StockLot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockLotRepository extends JpaRepository<StockLot, Long> {
    List<StockLot> findAllByProductId(Long productId);
}
