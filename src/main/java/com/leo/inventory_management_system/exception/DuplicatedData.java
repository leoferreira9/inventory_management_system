package com.leo.inventory_management_system.exception;

public class DuplicatedData extends RuntimeException {
    public DuplicatedData(String message) {
        super(message);
    }
}
