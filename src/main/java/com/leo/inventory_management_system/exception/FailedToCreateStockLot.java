package com.leo.inventory_management_system.exception;

public class FailedToCreateStockLot extends RuntimeException {
    public FailedToCreateStockLot(String message) {
        super(message);
    }
}
