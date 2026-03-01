package com.leo.inventory_management_system.service;

import com.leo.inventory_management_system.dto.stockLot.StockLotRequest;
import com.leo.inventory_management_system.dto.stockLot.StockLotResponse;
import com.leo.inventory_management_system.entity.Product;
import com.leo.inventory_management_system.entity.StockLot;
import com.leo.inventory_management_system.exception.EntityNotFound;
import com.leo.inventory_management_system.exception.FailedToCreateStockLot;
import com.leo.inventory_management_system.mapper.StockLotMapper;
import com.leo.inventory_management_system.repository.ProductRepository;
import com.leo.inventory_management_system.repository.StockLotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockLotService {

    private final StockLotMapper mapper;
    private final StockLotRepository repository;
    private final ProductRepository productRepository;

    public StockLotService(StockLotMapper mapper, StockLotRepository repository, ProductRepository productRepository){
        this.mapper = mapper;
        this.repository = repository;
        this.productRepository = productRepository;
    }

    private StockLot findStockLotOrThrow(Long id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFound("Stock lot not found with ID: " + id));
    }

    private Product findProductOrThrow(Long id){
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFound("Product not found with ID: " + id));
    }

    public StockLotResponse create (StockLotRequest request){
        Product productExists = findProductOrThrow(request.getProductId());

        if(request.getQuantity() < 0) throw new FailedToCreateStockLot("Stock lot quantity must be greater than or equal to zero");

        StockLot stockLot = mapper.toEntity(request);
        stockLot.setProduct(productExists);
        stockLot.setCreatedAt(LocalDateTime.now());

        StockLot savedStockLot = repository.save(stockLot);
        return mapper.toDto(savedStockLot);
    }

    public StockLotResponse findById(Long id){
        StockLot stockLotExists = findStockLotOrThrow(id);
        return mapper.toDto(stockLotExists);
    }

    public List<StockLotResponse> findAll(){
        return repository.findAll().stream().map(mapper::toDto).toList();
    }
}
