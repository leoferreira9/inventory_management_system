package com.leo.inventory_management_system.exception.handler;

import java.time.LocalDateTime;

public record ApiErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {}
