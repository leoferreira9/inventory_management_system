package com.leo.inventory_management_system.exception.handler;

import com.leo.inventory_management_system.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ApiErrorResponse buildApiErrorResponse(HttpStatus status, String message, WebRequest req){
        return new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                req.getDescription(false).replace("uri=", "")
        );
    }

    @ExceptionHandler(DisabledProduct.class)
    public ResponseEntity<ApiErrorResponse> handleDisabledProduct(DisabledProduct ex, WebRequest req){
        log.warn("Disabled product error: {}", ex.getMessage());

        ApiErrorResponse error = buildApiErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Disabled Product", req);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(DuplicatedData.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicatedData(DuplicatedData ex, WebRequest req){
        log.warn("Duplicated data error: {}", ex.getMessage());

        ApiErrorResponse error = buildApiErrorResponse(HttpStatus.CONFLICT, "Duplicated Data", req);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(EntityNotFound.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFound(EntityNotFound ex, WebRequest req){
        log.warn("Resource not found error: {}", ex.getMessage());

        ApiErrorResponse error = buildApiErrorResponse(HttpStatus.NOT_FOUND, "Resource Not Found", req);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(FailedDisablingProduct.class)
    public ResponseEntity<ApiErrorResponse> handleFailedDisablingProduct(FailedDisablingProduct ex, WebRequest req){
        log.warn("Failed disabling product error: {}", ex.getMessage());

        ApiErrorResponse error = buildApiErrorResponse(HttpStatus.CONFLICT, "Failed Disabling Product", req);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InvalidDate.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidDate(InvalidDate ex, WebRequest req){
        log.warn("Invalid date error: {}", ex.getMessage());

        ApiErrorResponse error = buildApiErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Date", req);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InvalidStockLotProductMismatch.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidStockLotProductMismatch(InvalidStockLotProductMismatch ex, WebRequest req){
        log.warn("Invalid stock lot product mismatch error: {}", ex.getMessage());

        ApiErrorResponse error = buildApiErrorResponse(HttpStatus.CONFLICT, "Invalid Stock Lot Product Mismatch", req);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InvalidStockMovementReason.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidStockMovementReason(InvalidStockMovementReason ex, WebRequest req){
        log.warn("Invalid stock movement reason error: {}", ex.getMessage());

        ApiErrorResponse error = buildApiErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Stock Movement Reason", req);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NullParameter.class)
    public ResponseEntity<ApiErrorResponse> handleNullParameter(NullParameter ex, WebRequest req){
        log.warn("Null parameter error: {}", ex.getMessage());

        ApiErrorResponse error = buildApiErrorResponse(HttpStatus.BAD_REQUEST, "Null Parameter", req);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(QuantityUnavailable.class)
    public ResponseEntity<ApiErrorResponse> handleQuantityUnavailable(QuantityUnavailable ex, WebRequest req){
        log.warn("Quantity unavailable error: {}", ex.getMessage());

        ApiErrorResponse error = buildApiErrorResponse(HttpStatus.CONFLICT, "Quantity Unavailable", req);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest req){
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();

        String message = String.join(", ", errors);

        log.warn("Validation error at {}: {}", req.getDescription(false), message);

        ApiErrorResponse error = buildApiErrorResponse(HttpStatus.BAD_REQUEST, message, req);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest req){
        log.warn("Invalid request body error: {}", ex.getMessage());

        ApiErrorResponse error = buildApiErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Request Body", req);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest req){
        log.warn("Invalid value for parameter error: {}", ex.getMessage());

        ApiErrorResponse error = buildApiErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Value For Parameter: " + ex.getName(), req);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception ex, WebRequest req){
        log.error("Unexpected error occurred: {}", ex.getMessage());

        ApiErrorResponse error = buildApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected Error Occurred", req);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}