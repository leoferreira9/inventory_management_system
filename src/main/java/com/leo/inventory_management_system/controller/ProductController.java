package com.leo.inventory_management_system.controller;

import com.leo.inventory_management_system.dto.product.ProductRequest;
import com.leo.inventory_management_system.dto.product.ProductResponse;
import com.leo.inventory_management_system.dto.product.UpdateProductRequest;
import com.leo.inventory_management_system.dto.product.UpdateProductStatusRequest;
import com.leo.inventory_management_system.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request){

        ProductResponse productResponse = service.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productResponse.getId())
                .toUri();

        return ResponseEntity.created(location).body(productResponse);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductResponse> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateProductStatusRequest request){
        return ResponseEntity.ok().body(service.updateStatus(id, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request){
        return ResponseEntity.ok().body(service.update(id, request));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }
}
