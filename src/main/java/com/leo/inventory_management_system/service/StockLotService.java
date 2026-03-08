package com.leo.inventory_management_system.service;

import com.leo.inventory_management_system.dto.product.ProductStockQuantityResponse;
import com.leo.inventory_management_system.dto.stockLot.StockLotRequest;
import com.leo.inventory_management_system.dto.stockLot.StockLotResponse;
import com.leo.inventory_management_system.entity.Product;
import com.leo.inventory_management_system.entity.StockLot;
import com.leo.inventory_management_system.exception.EntityNotFound;
import com.leo.inventory_management_system.exception.InvalidDate;
import com.leo.inventory_management_system.exception.QuantityUnavailable;
import com.leo.inventory_management_system.mapper.StockLotMapper;
import com.leo.inventory_management_system.repository.StockLotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockLotService {

    private final StockLotMapper mapper;
    private final StockLotRepository repository;
    private final ProductService productService;

    public StockLotService(StockLotMapper mapper, StockLotRepository repository, ProductService productService){
        this.mapper = mapper;
        this.repository = repository;
        this.productService = productService;
    }

    public StockLot findStockLotOrThrow(Long id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFound("Stock lot not found with ID: " + id));
    }

    public StockLotResponse create (StockLotRequest request){
        Product productExists = productService.findProductOrThrow(request.getProductId());

        if(request.getQuantity() < 0) throw new QuantityUnavailable("Stock lot quantity must be greater than or equal to zero");
        if(!request.getExpiryDate().isAfter(LocalDate.now()))
            throw new InvalidDate("Product could not be added to stock. Expiration date has passed.");

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

    public ProductStockQuantityResponse sumProductStockQuantity (Long productId){
        Product productExists = productService.findProductOrThrow(productId);
        Integer stockQuantity = repository.sumProductQuantityStock(productId);
        return new ProductStockQuantityResponse(productId,productExists.getName(), productExists.getSku(), stockQuantity);
    }
}
