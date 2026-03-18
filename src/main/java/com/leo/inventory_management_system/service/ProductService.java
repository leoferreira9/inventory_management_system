package com.leo.inventory_management_system.service;

import com.leo.inventory_management_system.dto.product.*;
import com.leo.inventory_management_system.entity.Product;
import com.leo.inventory_management_system.entity.StockLot;
import com.leo.inventory_management_system.exception.DuplicatedData;
import com.leo.inventory_management_system.exception.EntityNotFound;
import com.leo.inventory_management_system.exception.FailedDisablingProduct;
import com.leo.inventory_management_system.mapper.ProductMapper;
import com.leo.inventory_management_system.repository.ProductRepository;
import com.leo.inventory_management_system.repository.StockLotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductMapper mapper;
    private final ProductRepository repository;
    private final StockLotRepository stockLotRepository;

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductMapper mapper, ProductRepository repository, StockLotRepository stockLotRepository){
        this.mapper = mapper;
        this.repository = repository;
        this.stockLotRepository = stockLotRepository;
    }

    public Product findProductOrThrow(Long id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFound("Product not found with ID: " + id));
    }

    public ProductResponse create(ProductRequest request){
        log.info("Creating product with SKU: {}", request.getSku());

        Product productSkuAlreadyExists = repository.findProductBySku(request.getSku());
        if(productSkuAlreadyExists != null) throw new DuplicatedData("A product already exists with this SKU: " + request.getSku());

        Product product = mapper.toEntity(request);
        product.setActive(true);

        Product savedProduct = repository.save(product);
        log.info("Product created with ID: {}", savedProduct.getId());
        return mapper.toDto(savedProduct);
    }

    public ProductResponse findById(Long id){
        log.info("Finding product by ID: {}", id);

        Product productExists = findProductOrThrow(id);
        log.info("Product found with ID: {}", id);
        return mapper.toDto(productExists);
    }

    public List<ProductResponse> findAll(){
        List<ProductResponse> products = repository.findAll().stream().map(mapper::toDto).toList();
        log.info("Found {} products", products.size());
        return products;
    }

    public Page<ProductResponse> searchProducts(String search, Pageable pageable){
        log.info("Searching products with filter: {}", search);

        Page<Product> pageableResult;
        if(search == null || search.isEmpty()){
            pageableResult = repository.findAll(pageable);
        } else {
            pageableResult = repository.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(search, search, pageable);
        }

        log.info("Number of filtered products found: {}", pageableResult.getTotalElements());
        return pageableResult.map(mapper::toDto);
    }

    public ProductResponse update(Long id, UpdateProductRequest request){
        log.info("Updating product. ID: {}", id);

        Product productExists = findProductOrThrow(id);

        Product productSkuAlreadyExists = repository.findProductBySku(request.getSku());
        if(productSkuAlreadyExists != null && !productSkuAlreadyExists.getId().equals(id)) throw new DuplicatedData("A product already exists with this SKU: " + request.getSku());

        productExists.setName(request.getName());
        productExists.setDescription(request.getDescription());
        productExists.setPrice(request.getPrice());
        productExists.setSku(request.getSku());

        Product savedProduct = repository.save(productExists);

        log.info("Product updated. ID: {}", id);
        return mapper.toDto(savedProduct);
    }

    public ProductResponse updateStatus(Long id, UpdateProductStatusRequest request){
        log.info("Updating product status. ID: {}, active: {}", id, request.getActive());

        Product productExists = findProductOrThrow(id);

        int productStockQuantity = stockLotRepository.findAllByProductId(id).stream().map(StockLot::getQuantity).reduce(0, Integer::sum);
        if(productStockQuantity > 0 && !request.getActive()) throw new FailedDisablingProduct("Failure to disable product, it contains " + productStockQuantity + " units in stock");

        productExists.setActive(request.getActive());

        Product savedProduct = repository.save(productExists);

        log.info("Product status updated to {}. ID: {}", request.getActive(), id);
        return mapper.toDto(savedProduct);
    }

}
