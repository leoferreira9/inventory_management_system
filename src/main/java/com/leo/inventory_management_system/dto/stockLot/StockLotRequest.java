package com.leo.inventory_management_system.dto.stockLot;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class StockLotRequest {

    @Schema(description = "Product ID", example = "9")
    @NotNull
    private Long productId;

    @Schema(description = "Product batch code", example = "COCA-500-001")
    @NotBlank
    @Size(max = 100)
    private String batchCode;

    @Schema(description = "Product expiry date", example = "2026-03-15")
    @NotNull
    private LocalDate expiryDate;

    @Schema(description = "Product quantity", example = "35")
    @NotNull
    @Min(0)
    private int quantity;

    public StockLotRequest(){}

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
