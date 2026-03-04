package com.leo.inventory_management_system.exception;

public class InvalidStockMovementReason extends RuntimeException {
    public InvalidStockMovementReason(String message) {
        super(message);
    }
}
