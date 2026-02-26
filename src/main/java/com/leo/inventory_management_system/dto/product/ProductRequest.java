package com.leo.inventory_management_system.dto.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ProductRequest {

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

    @NotNull
    private Boolean active;

    public ProductRequest(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
