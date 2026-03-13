package com.leo.inventory_management_system.exception.handler;

import com.leo.inventory_management_system.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public ApiErrorResponse buildApiErrorResponse(int status, String error, String message, WebRequest req){
        return new ApiErrorResponse(
                LocalDateTime.now(),
                status,
                error,
                message,
                req.getDescription(false).replace("uri=", "")
        );
    }

    @ExceptionHandler(DisabledProduct.class)
    public ResponseEntity<ApiErrorResponse> handleDisabledProduct(DisabledProduct ex, WebRequest req){
        ApiErrorResponse error = buildApiErrorResponse(400, "Disabled Product", ex.getMessage(), req);
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(DuplicatedData.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicatedData(DuplicatedData ex, WebRequest req){
        ApiErrorResponse error = buildApiErrorResponse(409, "Duplicated Data", ex.getMessage(), req);
        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(EntityNotFound.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFound(EntityNotFound ex, WebRequest req){
        ApiErrorResponse error = buildApiErrorResponse(404, "Entity not Found", ex.getMessage(), req);
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(FailedDisablingProduct.class)
    public ResponseEntity<ApiErrorResponse> handleFailedDisablingProduct(FailedDisablingProduct ex, WebRequest req){
        ApiErrorResponse error = buildApiErrorResponse(409, "Failed Disabling Product", ex.getMessage(), req);
        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(InvalidDate.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidDate(InvalidDate ex, WebRequest req){
        ApiErrorResponse error = buildApiErrorResponse(400, "Invalid Date", ex.getMessage(), req);
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(InvalidStockLotProductMismatch.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidStockLotProductMismatch(InvalidStockLotProductMismatch ex, WebRequest req){
        ApiErrorResponse error = buildApiErrorResponse(409, "Invalid Stock Lot Product Mismatch", ex.getMessage(), req);
        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(InvalidStockMovementReason.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidStockMovementReason(InvalidStockMovementReason ex, WebRequest req){
        ApiErrorResponse error = buildApiErrorResponse(400, "Invalid Stock Movement Reason", ex.getMessage(), req);
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(NullParameter.class)
    public ResponseEntity<ApiErrorResponse> handleNullParameter(NullParameter ex, WebRequest req){
        ApiErrorResponse error = buildApiErrorResponse(400, "Null Parameter", ex.getMessage(), req);
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(QuantityUnavailable.class)
    public ResponseEntity<ApiErrorResponse> handleQuantityUnavailable(QuantityUnavailable ex, WebRequest req){
        ApiErrorResponse error = buildApiErrorResponse(409, "Quantity Unavailable", ex.getMessage(), req);
        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest req){

        List<String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();

        String message = String.join(", ", erros);

        ApiErrorResponse error = buildApiErrorResponse(400, "Method Argument Not Valid", message, req);
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest req){
        ApiErrorResponse error = buildApiErrorResponse(400, "Invalid request body", ex.getMessage(), req);
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest req){
        ApiErrorResponse error = buildApiErrorResponse(400, "Invalid parameter type", ex.getMessage(), req);
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception ex, WebRequest req){
        ApiErrorResponse error = buildApiErrorResponse(500, "Internal Server Error", ex.getMessage(), req);
        return ResponseEntity.status(500).body(error);
    }
}