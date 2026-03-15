package com.leo.inventory_management_system.controller;

import com.leo.inventory_management_system.dto.product.*;
import com.leo.inventory_management_system.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Product controller", description = "Endpoints for managing products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service){
        this.service = service;
    }

    @Operation(summary = "Create a new product")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created"),
            @ApiResponse(responseCode = "409", description = "Duplicate product")
    })
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request){

        ProductResponse productResponse = service.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productResponse.getId())
                .toUri();

        return ResponseEntity.created(location).body(productResponse);
    }


    @Operation(summary = "Find all products")
    @ApiResponse(responseCode = "200", description = "Products found")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @Operation(summary = "Find products by name or sku")
    @ApiResponse(responseCode = "200", description = "Products found")
    @GetMapping("/filter")
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @Parameter(description = "Search query to find products by name or SKU") @RequestParam(required = false) String search,
            @Parameter(description = "Pagination parameters") Pageable pageable){
        return ResponseEntity.ok().body(service.searchProducts(search, pageable));
    }

    @Operation(summary = "Updates the product to active or inactive")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "409", description = "Failed disabling product")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductResponse> updateStatus(
            @Parameter(description = "Product ID") @PathVariable Long id,
            @Valid @RequestBody UpdateProductStatusRequest request){
        return ResponseEntity.ok().body(service.updateStatus(id, request));
    }

    @Operation(summary = "Updates a product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "409", description = "Duplicated product")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(
            @Parameter(description = "Product ID") @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request){
        return ResponseEntity.ok().body(service.update(id, request));
    }


    @Operation(summary = "Find product by ID")
    @ApiResponse(responseCode = "200", description = "Product found")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@Parameter(description = "Product ID") @PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

}
