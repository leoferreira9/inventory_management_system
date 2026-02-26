package com.leo.inventory_management_system.dto.StockMovementLot;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class StockMovementLotRequest {

    @NotNull
    private Long movementId;

    @NotNull
    private Long lotId;

    @NotNull
    @Min(1)
    private int quantity;

    public StockMovementLotRequest(){}

    public Long getMovementId() {
        return movementId;
    }

    public void setMovementId(Long movementId) {
        this.movementId = movementId;
    }

    public Long getLotId() {
        return lotId;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
