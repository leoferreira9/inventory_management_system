package com.leo.inventory_management_system.service;

import com.leo.inventory_management_system.dto.stockMovement.StockMovementRequest;
import com.leo.inventory_management_system.dto.stockMovement.StockMovementResponse;
import com.leo.inventory_management_system.entity.Product;
import com.leo.inventory_management_system.entity.StockMovement;
import com.leo.inventory_management_system.enums.MovementReason;
import com.leo.inventory_management_system.enums.MovementType;
import com.leo.inventory_management_system.exception.EntityNotFound;
import com.leo.inventory_management_system.exception.InvalidDate;
import com.leo.inventory_management_system.exception.InvalidStockMovementReason;
import com.leo.inventory_management_system.exception.QuantityUnavailable;
import com.leo.inventory_management_system.mapper.StockMovementMapper;
import com.leo.inventory_management_system.repository.StockMovementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class StockMovementService {

    private final StockMovementRepository repository;
    private final StockMovementMapper mapper;
    private final ProductService productService;

    public StockMovementService(StockMovementRepository repository, StockMovementMapper mapper, ProductService productService){
        this.repository = repository;
        this.mapper = mapper;
        this.productService = productService;
    }

    public StockMovement findStockMovementOrThrow(Long id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFound("Stock movement not found with ID: " + id));
    }

    private static final Map<MovementType, Set<MovementReason>> map = Map.of(
            MovementType.IN, EnumSet.of(MovementReason.PURCHASE, MovementReason.INITIAL_STOCK, MovementReason.RETURN_FROM_CLIENT),
            MovementType.OUT, EnumSet.of(MovementReason.SALE, MovementReason.EXPIRED, MovementReason.DAMAGE),
            MovementType.ADJUST, EnumSet.of(MovementReason.ADJUSTMENT_IN, MovementReason.ADJUSTMENT_OUT)
    );


    public StockMovementResponse create(StockMovementRequest request){
        Product product = productService.findProductOrThrow(request.getProductId());

        if(request.getQuantity() <= 0) throw new QuantityUnavailable("Stock movement quantity must be greater than zero");
        if(request.getOccurredAt().isAfter(LocalDateTime.now())) throw new InvalidDate("The date the event occurred cannot be later than the current date");

        Set<MovementReason> allowedReasons = map.get(request.getType());

        StockMovement stockMovement = mapper.toEntity(request);

        if(allowedReasons == null || !allowedReasons.contains(request.getReason()))
            throw new InvalidStockMovementReason("The type of movement does not correspond to the reason: " + request.getType() + " -> " + allowedReasons);


        stockMovement.setProduct(product);
        stockMovement.setCreatedAt(LocalDateTime.now());

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
