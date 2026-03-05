package com.leo.inventory_management_system.dto.stockMovement;

import com.leo.inventory_management_system.enums.MovementReason;
import com.leo.inventory_management_system.enums.MovementType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class StockMovementRequest {

    @NotNull
    private MovementType type;

    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    private int quantity;

    @NotNull
    private LocalDateTime occurredAt;

    @NotNull
    private MovementReason reason;

    private String reference;

    @NotNull
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
