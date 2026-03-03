package com.leo.inventory_management_system.mapper;

import com.leo.inventory_management_system.dto.stockMovement.StockMovementRequest;
import com.leo.inventory_management_system.dto.stockMovement.StockMovementResponse;
import com.leo.inventory_management_system.entity.StockMovement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMovementMapper {

    StockMovement toEntity (StockMovementRequest request);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    StockMovementResponse toDto (StockMovement stockMovement);

}
