package com.leo.inventory_management_system.dto.stockMovementLot;


import com.leo.inventory_management_system.enums.MovementType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StockMovementLotResponse {

    private Long id;
    private Long movementId;
    private Long lotId;
    private String batchCode;
    private LocalDate expiryDate;
    private MovementType type;
    private LocalDateTime occurredAt;
    private int quantity;

    public StockMovementLotResponse(){}

    public Long getId() {
        return id;
    }

    public Long getMovementId() {
        return movementId;
    }

    public Long getLotId() {
        return lotId;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public MovementType getType() {
        return type;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public int getQuantity() {
        return quantity;
    }
}
