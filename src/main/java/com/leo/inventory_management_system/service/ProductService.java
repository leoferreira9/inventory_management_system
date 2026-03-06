package com.leo.inventory_management_system.service;

import com.leo.inventory_management_system.dto.product.ProductRequest;
import com.leo.inventory_management_system.dto.product.ProductResponse;
import com.leo.inventory_management_system.dto.product.UpdateProductRequest;
import com.leo.inventory_management_system.dto.product.UpdateProductStatusRequest;
import com.leo.inventory_management_system.entity.Product;
import com.leo.inventory_management_system.entity.StockLot;
import com.leo.inventory_management_system.exception.DuplicatedData;
import com.leo.inventory_management_system.exception.EntityNotFound;
import com.leo.inventory_management_system.exception.FailedDisablingProduct;
import com.leo.inventory_management_system.mapper.ProductMapper;
import com.leo.inventory_management_system.repository.ProductRepository;
import com.leo.inventory_management_system.repository.StockLotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    private final ProductMapper mapper;
    private final ProductRepository repository;
    private final StockLotRepository stockLotRepository;

    public ProductService(ProductMapper mapper, ProductRepository repository, StockLotRepository stockLotRepository){
        this.mapper = mapper;
        this.repository = repository;
        this.stockLotRepository = stockLotRepository;
    }

    public Product findProductOrThrow(Long id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFound("Product not found with ID: " + id));
    }

    public ProductResponse create(ProductRequest request){
        Product productSkuAlreadyExists = repository.findProductBySku(request.getSku());
        if(productSkuAlreadyExists != null) throw new DuplicatedData("A product already exists with this SKU: " + request.getSku());

        Product product = mapper.toEntity(request);
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());

        Product savedProduct = repository.save(product);
        return mapper.toDto(savedProduct);
    }

    public ProductResponse findById(Long id){
        Product productExists = findProductOrThrow(id);
        return mapper.toDto(productExists);
    }

    public List<ProductResponse> findAll(){
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public ProductResponse update(Long id, UpdateProductRequest request){
        Product productExists = findProductOrThrow(id);

        Product productSkuAlreadyExists = repository.findProductBySku(request.getSku());
        if(productSkuAlreadyExists != null && !productSkuAlreadyExists.getId().equals(id)) throw new DuplicatedData("A product already exists with this SKU: " + request.getSku());

        productExists.setName(request.getName());
        productExists.setDescription(request.getDescription());
        productExists.setPrice(request.getPrice());
        productExists.setSku(request.getSku());
        productExists.setUpdatedAt(LocalDateTime.now());

        Product savedProduct = repository.save(productExists);
        return mapper.toDto(savedProduct);
    }

    public ProductResponse updateStatus(Long id, UpdateProductStatusRequest request){
        Product productExists = findProductOrThrow(id);

        int productStockQuantity = stockLotRepository.findAllByProductId(id).stream().map(StockLot::getQuantity).reduce(0, Integer::sum);
        if(productStockQuantity > 0 && !request.getActive()) throw new FailedDisablingProduct("Failure to disable product, it contains " + productStockQuantity + " units in stock");

        productExists.setActive(request.getActive());
        productExists.setUpdatedAt(LocalDateTime.now());

        Product savedProduct = repository.save(productExists);
        return mapper.toDto(savedProduct);
    }

}
