package com.leo.inventory_management_system.repository;

import com.leo.inventory_management_system.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {}
