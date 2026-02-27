package com.leo.inventory_management_system.mapper;

import com.leo.inventory_management_system.dto.product.ProductRequest;
import com.leo.inventory_management_system.dto.product.ProductResponse;
import com.leo.inventory_management_system.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity (ProductRequest request);
    ProductResponse toDto (Product product);
}
