package com.leo.inventory_management_system.service;

import com.leo.inventory_management_system.dto.product.ProductStockQuantityResponse;
import com.leo.inventory_management_system.dto.stockLot.StockLotRequest;
import com.leo.inventory_management_system.dto.stockLot.StockLotResponse;
import com.leo.inventory_management_system.entity.Product;
import com.leo.inventory_management_system.entity.StockLot;
import com.leo.inventory_management_system.exception.*;
import com.leo.inventory_management_system.mapper.StockLotMapper;
import com.leo.inventory_management_system.repository.StockLotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StockLotService {

    private final StockLotMapper mapper;
    private final StockLotRepository repository;
    private final ProductService productService;

    private static final Logger log = LoggerFactory.getLogger(StockLotService.class);

    public StockLotService(StockLotMapper mapper, StockLotRepository repository, ProductService productService){
        this.mapper = mapper;
        this.repository = repository;
        this.productService = productService;
    }

    public StockLot findStockLotOrThrow(Long id){
        if(id == null) throw new NullParameter("This action requires the Stock lot ID");
        return repository.findById(id).orElseThrow(() -> new EntityNotFound("Stock lot not found with ID: " + id));
    }

    public StockLotResponse create (StockLotRequest request){
        log.info("Creating stock lot with batchCode: {}", request.getBatchCode());

        Product productExists = productService.findProductOrThrow(request.getProductId());

        if(!productExists.isActive()) throw new DisabledProduct("Failure creating stock lot, product disabled.");

        Optional<StockLot> stockLotAlreadyExists = repository.findByProductIdAndBatchCode(request.getProductId(), request.getBatchCode());
        if(stockLotAlreadyExists.isPresent()) throw new DuplicatedData("A stock lot already exists for this product with batch code: " + request.getBatchCode());

        if(request.getQuantity() < 0) throw new QuantityUnavailable("Stock lot quantity must be greater than or equal to zero");
        if(!request.getExpiryDate().isAfter(LocalDate.now()))
            throw new InvalidDate("Product could not be added to stock. Expiration date has passed.");

        StockLot stockLot = mapper.toEntity(request);
        stockLot.setProduct(productExists);

        StockLot savedStockLot = repository.save(stockLot);

        log.info("Stock lot created with ID: {}", savedStockLot.getId());
        return mapper.toDto(savedStockLot);
    }

    public StockLotResponse findById(Long id){
        log.info("Finding stock lot with ID: {}", id);

        StockLot stockLotExists = findStockLotOrThrow(id);

        log.info("Stock lot found with ID: {}", id);
        return mapper.toDto(stockLotExists);
    }

    public List<StockLotResponse> findAll(){
        List<StockLotResponse> stockLots = repository.findAll().stream().map(mapper::toDto).toList();
        log.info("Found {} stock lots", stockLots.size());
        return stockLots;
    }

    public ProductStockQuantityResponse sumProductStockQuantity (Long productId){
        log.info("Calculating total stock for product ID: {}", productId);

        Product productExists = productService.findProductOrThrow(productId);
        Integer stockQuantity = repository.sumProductQuantityStock(productId);

        log.info("Total stock for product ID {}: {}", productId, stockQuantity);
        return new ProductStockQuantityResponse(productId,productExists.getName(), productExists.getSku(), stockQuantity != null ? stockQuantity : 0);
    }
}
