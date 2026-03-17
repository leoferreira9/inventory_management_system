package com.leo.inventory_management_system.service;

import com.leo.inventory_management_system.dto.stockMovement.StockMovementRequest;
import com.leo.inventory_management_system.dto.stockMovement.StockMovementResponse;
import com.leo.inventory_management_system.dto.stockMovementLot.StockMovementLotRequest;
import com.leo.inventory_management_system.entity.Product;
import com.leo.inventory_management_system.entity.StockLot;
import com.leo.inventory_management_system.entity.StockMovement;
import com.leo.inventory_management_system.enums.MovementReason;
import com.leo.inventory_management_system.enums.MovementType;
import com.leo.inventory_management_system.exception.*;
import com.leo.inventory_management_system.mapper.StockMovementLotMapper;
import com.leo.inventory_management_system.mapper.StockMovementMapper;
import com.leo.inventory_management_system.repository.StockLotRepository;
import com.leo.inventory_management_system.repository.StockMovementLotRepository;
import com.leo.inventory_management_system.repository.StockMovementRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class StockMovementService {

    private final StockMovementRepository repository;
    private final StockMovementMapper mapper;
    private final ProductService productService;
    private final StockLotService stockLotService;
    private final StockLotRepository stockLotRepository;
    private final StockMovementLotRepository stockMovementLotRepository;
    private final StockMovementLotMapper stockMovementLotMapper;

    public StockMovementService(StockMovementRepository repository,
                                StockMovementMapper mapper,
                                ProductService productService,
                                StockLotService stockLotService,
                                StockLotRepository stockLotRepository,
                                StockMovementLotRepository stockMovementLotRepository,
                                StockMovementLotMapper stockMovementLotMapper){
        this.repository = repository;
        this.mapper = mapper;
        this.productService = productService;
        this.stockLotService = stockLotService;
        this.stockLotRepository = stockLotRepository;
        this.stockMovementLotRepository = stockMovementLotRepository;
        this.stockMovementLotMapper = stockMovementLotMapper;
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

    public void processExitByStockLots(int quantity, StockLot stockLotExists, StockMovement savedStockMovement){
        decreaseStock(quantity, stockLotExists);
        stockLotRepository.save(stockLotExists);

        StockMovementLotRequest stockMovementLotRequest = new StockMovementLotRequest(savedStockMovement.getId(),stockLotExists.getId(), quantity);
        stockMovementLotRepository.save(stockMovementLotMapper.toEntity(stockMovementLotRequest));
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
            throw new InvalidStockMovementReason("The type of movement does not correspond to the reason: the options for (" + request.getType() + ") are -> " + allowedReasons);

        stockMovement.setProduct(product);

        MovementType type = request.getType();

        StockMovement savedStockMovement = repository.save(stockMovement);

        if(type == MovementType.ADJUST){
            StockLot stockLotExists = stockLotService.findStockLotOrThrow(request.getStockLotId());
            if(request.getReason() == MovementReason.ADJUSTMENT_IN){
                processEntry(request, stockLotExists);
            } else {
                processExit(request, stockLotExists);
            }
        } else if(type == MovementType.IN){
            StockLot stockLotExists = stockLotService.findStockLotOrThrow(request.getStockLotId());
            processEntry(request, stockLotExists);
        } else if (type == MovementType.OUT){
            List<StockLot> productLots = stockLotRepository.findAllOrderedByExpiryDate(request.getProductId());
            Integer totalStockResult = stockLotRepository.sumProductQuantityStock(product.getId());

            int totalStock = totalStockResult != null ? totalStockResult : 0;
            int remainingQuantity = request.getQuantity();

            if(totalStock < remainingQuantity) throw new QuantityUnavailable("Product with insufficient stock. Available quantity: " + totalStock);

            for(StockLot sl: productLots){
                if(sl.getQuantity() >= remainingQuantity){
                    processExitByStockLots(remainingQuantity, sl, savedStockMovement);
                    break;
                } else {
                    int quantityToRemove = sl.getQuantity();
                    processExitByStockLots(quantityToRemove, sl, savedStockMovement);
                    remainingQuantity -= quantityToRemove;
                }
            }
        }


        return mapper.toDto(savedStockMovement);
    }

    public StockMovementResponse findById(Long id){
        StockMovement stockMovementExists = findStockMovementOrThrow(id);
        return mapper.toDto(stockMovementExists);
    }

    public List<StockMovementResponse> findAll(){
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public Page<StockMovementResponse> findWithFilters(Long productId,
                                                       String productName,
                                                       MovementType type,
                                                       MovementReason reason,
                                                       LocalDate startDate,
                                                       LocalDate endDate,
                                                       Pageable pageable
    ){
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(23, 59, 59) : null;

        Page<StockMovement> page = repository.findWithFilters(productId, productName, type, reason, startDateTime, endDateTime, pageable);
        return page.map(mapper::toDto);
    }
}
