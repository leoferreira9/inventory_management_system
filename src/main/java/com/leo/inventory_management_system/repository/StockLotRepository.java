package com.leo.inventory_management_system.repository;

import com.leo.inventory_management_system.entity.StockLot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockLotRepository extends JpaRepository<StockLot, Long> {}
