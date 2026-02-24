package com.leo.inventory_management_system.repository;

import com.leo.inventory_management_system.entity.StockMovementLot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockMovementLotRepository extends JpaRepository<StockMovementLot, Long> {}
