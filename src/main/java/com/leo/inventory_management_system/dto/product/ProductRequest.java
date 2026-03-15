package com.leo.inventory_management_system.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ProductRequest {

    @Schema(description = "Product name", example = "Whey Protein 1KG")
    @NotBlank
    @Size(max = 100)
    private String name;

    @Schema(description = "Product description", example = "Whey protein with 25 grams of protein per scoop")
    @Size(max = 150)
    private String description;

    @Schema(description = "Product price", example = "98.30")
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal price;

    @Schema(description = "Internal product code", example = "SUP-WHEY-1KG")
    @NotBlank
    @Size(max = 40)
    private String sku;

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
}
