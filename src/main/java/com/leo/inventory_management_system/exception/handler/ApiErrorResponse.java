package com.leo.inventory_management_system.exception.handler;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiErrorResponse(LocalDateTime timestamp, HttpStatus status, String error, String message, String path) {}
