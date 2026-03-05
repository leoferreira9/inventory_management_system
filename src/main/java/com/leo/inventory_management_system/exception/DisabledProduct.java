package com.leo.inventory_management_system.exception;

public class DisabledProduct extends RuntimeException {
    public DisabledProduct(String message) {
        super(message);
    }
}
