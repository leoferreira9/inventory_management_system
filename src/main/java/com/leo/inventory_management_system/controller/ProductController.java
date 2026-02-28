package com.leo.inventory_management_system.controller;

import com.leo.inventory_management_system.dto.product.ProductRequest;
import com.leo.inventory_management_system.dto.product.ProductResponse;
import com.leo.inventory_management_system.dto.product.UpdateProductRequest;
import com.leo.inventory_management_system.dto.product.UpdateProductStatusRequest;
import com.leo.inventory_management_system.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service){
        this.service = service;
    }

    @PostMapping("/create")
    public ProductResponse create(@Valid @RequestBody ProductRequest request){
        return service.create(request);
    }

    @GetMapping
    public List<ProductResponse> findAll(){
        return service.findAll();
    }

    @PatchMapping("/update/{id}/status")
    public ProductResponse updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateProductStatusRequest request){
        return service.updateStatus(id, request);
    }

    @PutMapping("/update/{id}")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request){
        return service.update(id, request);
    }


    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable Long id){
        return service.findById(id);
    }
}
