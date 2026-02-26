package com.leo.inventory_management_system.dto.stockLot;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StockLotResponse {

    private Long id;
    private Long productId;
    private String productName;
    private String batchCode;
    private LocalDate expiryDate;
    private int quantity;
    private LocalDateTime createdAt;

    public StockLotResponse(){}

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
