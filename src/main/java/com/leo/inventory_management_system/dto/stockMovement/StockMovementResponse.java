package com.leo.inventory_management_system.dto.stockMovement;

import com.leo.inventory_management_system.enums.MovementType;

import java.time.LocalDateTime;

public class StockMovementResponse {

    private Long id;
    private MovementType type;
    private Long productId;
    private String productName;
    private int quantity;
    private LocalDateTime occurredAt;
    private String reason;
    private String reference;
    private LocalDateTime createdAt;

    public StockMovementResponse(){}

    public Long getId() {
        return id;
    }

    public MovementType getType() {
        return type;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public String getReason() {
        return reason;
    }

    public String getReference() {
        return reference;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
