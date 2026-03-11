package com.leo.inventory_management_system.mapper;

import com.leo.inventory_management_system.dto.stockMovementLot.StockMovementLotRequest;
import com.leo.inventory_management_system.entity.StockMovementLot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMovementLotMapper {

    @Mapping(source = "lotId", target = "lot.id")
    @Mapping(source = "movementId", target = "movement.id")
    StockMovementLot toEntity(StockMovementLotRequest request);
}
