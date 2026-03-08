package com.leo.inventory_management_system.service;

import com.leo.inventory_management_system.dto.stockMovement.StockMovementRequest;
import com.leo.inventory_management_system.dto.stockMovement.StockMovementResponse;
import com.leo.inventory_management_system.entity.Product;
import com.leo.inventory_management_system.entity.StockLot;
import com.leo.inventory_management_system.entity.StockMovement;
import com.leo.inventory_management_system.enums.MovementReason;
import com.leo.inventory_management_system.enums.MovementType;
import com.leo.inventory_management_system.exception.*;
import com.leo.inventory_management_system.mapper.StockMovementMapper;
import com.leo.inventory_management_system.repository.StockLotRepository;
import com.leo.inventory_management_system.repository.StockMovementRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class StockMovementService {

    private final StockMovementRepository repository;
    private final StockMovementMapper mapper;
    private final ProductService productService;
    private final StockLotService stockLotService;
    private final StockLotRepository stockLotRepository;

    public StockMovementService(StockMovementRepository repository, StockMovementMapper mapper, ProductService productService, StockLotService stockLotService, StockLotRepository stockLotRepository){
        this.repository = repository;
        this.mapper = mapper;
        this.productService = productService;
        this.stockLotService = stockLotService;
        this.stockLotRepository = stockLotRepository;
    }

    public StockMovement findStockMovementOrThrow(Long id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFound("Stock movement not found with ID: " + id));
    }

    public void validateIdenticalProducts(StockMovementRequest request, StockLot stockLotExists){
        if(!stockLotExists.getProduct().getId().equals(request.getProductId()))
            throw new InvalidStockLotProductMismatch("Products from Stock movement and Stock lot are different");
    }

    private static final Map<MovementType, Set<MovementReason>> map = Map.of(
            MovementType.IN, EnumSet.of(MovementReason.PURCHASE, MovementReason.INITIAL_STOCK, MovementReason.RETURN_FROM_CLIENT),
            MovementType.OUT, EnumSet.of(MovementReason.SALE, MovementReason.EXPIRED, MovementReason.DAMAGE),
            MovementType.ADJUST, EnumSet.of(MovementReason.ADJUSTMENT_IN, MovementReason.ADJUSTMENT_OUT)
    );

    public void processEntry(StockMovementRequest request, StockLot stockLotExists){
        validateIdenticalProducts(request, stockLotExists);
        increaseStock(request.getQuantity(), stockLotExists);
        stockLotRepository.save(stockLotExists);
    }

    public void processExit(StockMovementRequest request, StockLot stockLotExists){
        validateIdenticalProducts(request, stockLotExists);
        decreaseStock(request.getQuantity(), stockLotExists);
        stockLotRepository.save(stockLotExists);
    }

    public void increaseStock(int quantity, StockLot stockLot){
        stockLot.setQuantity(stockLot.getQuantity() + quantity);
    }

    public void decreaseStock(int quantity, StockLot stockLot){
        if(quantity > stockLot.getQuantity()) throw new QuantityUnavailable("Insufficient stock. Available quantity: " + stockLot.getQuantity());
        stockLot.setQuantity(stockLot.getQuantity() - quantity);
    }

    @Transactional
    public StockMovementResponse create(StockMovementRequest request){
        Product product = productService.findProductOrThrow(request.getProductId());

        if(!product.isActive()) throw new DisabledProduct("Failure in stock movement, product disabled.");
        if(request.getQuantity() <= 0) throw new QuantityUnavailable("Stock movement quantity must be greater than zero");
        if(request.getOccurredAt().isAfter(LocalDateTime.now())) throw new InvalidDate("The date the event occurred cannot be later than the current date");

        Set<MovementReason> allowedReasons = map.get(request.getType());
        StockMovement stockMovement = mapper.toEntity(request);

        if(allowedReasons == null || !allowedReasons.contains(request.getReason()))
            throw new InvalidStockMovementReason("The type of movement does not correspond to the reason: " + request.getType() + " -> " + allowedReasons);

        stockMovement.setProduct(product);
        stockMovement.setCreatedAt(LocalDateTime.now());

        StockLot stockLotExists = stockLotService.findStockLotOrThrow(request.getStockLotId());
        MovementType type = request.getType();

        if(type == MovementType.ADJUST){
            if(request.getReason() == MovementReason.ADJUSTMENT_IN){
                processEntry(request, stockLotExists);
            } else {
                processExit(request, stockLotExists);
            }
        } else if(type == MovementType.IN){
            processEntry(request, stockLotExists);
        } else if (type == MovementType.OUT){
            processExit(request, stockLotExists);
        }

        StockMovement savedStockMovement = repository.save(stockMovement);

        return mapper.toDto(savedStockMovement);
    }

    public StockMovementResponse findById(Long id){
        StockMovement stockMovementExists = findStockMovementOrThrow(id);
        return mapper.toDto(stockMovementExists);
    }

    public List<StockMovementResponse> findAll(){
        return repository.findAll().stream().map(mapper::toDto).toList();
    }
}
