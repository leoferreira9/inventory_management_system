package com.leo.inventory_management_system.controller;

import com.leo.inventory_management_system.dto.stockMovement.StockMovementRequest;
import com.leo.inventory_management_system.dto.stockMovement.StockMovementResponse;
import com.leo.inventory_management_system.service.StockMovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stock-movements")
@Tag(name = "Stock movement controller", description = "Endpoint for managing stock movements")
public class StockMovementController {

    private final StockMovementService service;

    public StockMovementController(StockMovementService service){
        this.service = service;
    }

    @Operation(summary = "Create a new Stock movement")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Stock movement created"),
            @ApiResponse(responseCode = "400", description = "Disabled product, Invalid date or stock movement reason"),
            @ApiResponse(responseCode = "409", description = "Quantity unavailable")
    })
    @PostMapping
    public ResponseEntity<StockMovementResponse> create(@Valid @RequestBody StockMovementRequest request){
        StockMovementResponse response = service.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Find stock movement by ID")
    @ApiResponse(responseCode = "200", description = "Stock movement found")
    @GetMapping("/{id}")
    public ResponseEntity<StockMovementResponse> findById(@Parameter(description = "Stock movement ID") @PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @Operation(summary = "Find all Stock movements")
    @ApiResponse(responseCode = "200", description = "Stock movements found")
    @GetMapping
    public ResponseEntity<List<StockMovementResponse>> findAll(){
        return ResponseEntity.ok().body(service.findAll());
    }
}

