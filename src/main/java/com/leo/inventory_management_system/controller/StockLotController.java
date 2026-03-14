package com.leo.inventory_management_system.controller;

import com.leo.inventory_management_system.dto.product.ProductStockQuantityResponse;
import com.leo.inventory_management_system.dto.stockLot.StockLotRequest;
import com.leo.inventory_management_system.dto.stockLot.StockLotResponse;
import com.leo.inventory_management_system.service.StockLotService;
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
@RequestMapping("/stock-lots")
@Tag(name = "Stock lot controller", description = "Endpoints for managing stock lots")
public class StockLotController {

    private final StockLotService service;

    public StockLotController(StockLotService service) {
        this.service = service;
    }

    @Operation(summary = "Create a new stock lot")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Stock lot created"),
            @ApiResponse(responseCode = "400", description = "Disabled or expired lot"),
            @ApiResponse(responseCode = "409", description = "Duplicated stock lot or quantity unavailable"),
    })
    @PostMapping
    public ResponseEntity<StockLotResponse> create(@Parameter(description = "Data requested to create new stock lot") @Valid @RequestBody StockLotRequest request){
        StockLotResponse response = service.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Find stock lot by ID")
    @ApiResponse(responseCode = "200", description = "Stock lot found")
    @GetMapping("/{id}")
    public ResponseEntity<StockLotResponse> findById(@Parameter(description = "Stock lot ID") @PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @Operation(summary = "Find all stock lots")
    @ApiResponse(responseCode = "200", description = "Stock lots found")
    @GetMapping
    public ResponseEntity<List<StockLotResponse>> findAll(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @Operation(summary = "Calculates the total stock quantity of a product")
    @ApiResponse(responseCode = "200", description = "Total quantity calculated")
    @GetMapping("/{id}/stock")
    public ResponseEntity<ProductStockQuantityResponse> sumProductStockQuantity(@Parameter(description = "Product ID") @PathVariable Long id){
        return ResponseEntity.ok().body(service.sumProductStockQuantity(id));
    }
}
