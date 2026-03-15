package com.leo.inventory_management_system.dto.stockMovement;

import com.leo.inventory_management_system.enums.MovementReason;
import com.leo.inventory_management_system.enums.MovementType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class StockMovementRequest {

    @Schema(description = "Movement type", example = "IN / OUT / ADJUST")
    @NotNull
    private MovementType type;

    @Schema(description = "Product ID", example = "9")
    @NotNull
    private Long productId;

    @Schema(description = "Product quantity", example = "35")
    @NotNull
    @Min(1)
    private int quantity;

    @Schema(description = "Date when the movement occurred", example = "2026-03-15")
    @NotNull
    private LocalDateTime occurredAt;

    @Schema(description = "Movement reason", example = "PURCHASE / SALE / ADJUSTMENT_IN")
    @NotNull
    private MovementReason reason;

    @Schema(description = "Invoice number", example = "NF-323123")
    private String reference;

    @Schema(description = "Stock lot ID", example = "4")
    private Long stockLotId;

    public StockMovementRequest(){}

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }

    public MovementReason getReason() {
        return reason;
    }

    public void setReason(MovementReason reason) {
        this.reason = reason;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Long getStockLotId() {
        return stockLotId;
    }

    public void setStockLotId(Long stockLotId) {
        this.stockLotId = stockLotId;
    }
}
