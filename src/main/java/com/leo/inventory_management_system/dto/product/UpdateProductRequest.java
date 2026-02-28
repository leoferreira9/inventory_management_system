package com.leo.inventory_management_system.dto.product;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class UpdateProductRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 150)
    private String description;

    @NotNull
    @DecimalMin("0.0")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal price;

    @NotBlank
    @Size(max = 40)
    private String sku;

    public UpdateProductRequest(){}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getSku() {
        return sku;
    }
}
