package com.leo.inventory_management_system.exception;

public class QuantityUnavailable extends RuntimeException {
    public QuantityUnavailable(String message) {
        super(message);
    }
}
