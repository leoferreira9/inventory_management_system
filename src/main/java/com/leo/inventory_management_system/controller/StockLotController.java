package com.leo.inventory_management_system.controller;

import com.leo.inventory_management_system.dto.stockLot.StockLotRequest;
import com.leo.inventory_management_system.dto.stockLot.StockLotResponse;
import com.leo.inventory_management_system.service.StockLotService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stock-lots")
public class StockLotController {

    private final StockLotService service;

    public StockLotController(StockLotService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<StockLotResponse> create(@Valid @RequestBody StockLotRequest request){
        StockLotResponse response = service.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response)
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockLotResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<StockLotResponse>> findAll(){
        return ResponseEntity.ok().body(service.findAll());
    }
}
