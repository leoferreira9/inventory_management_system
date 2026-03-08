package com.leo.inventory_management_system.dto.product;

public class ProductStockQuantityResponse {

    private Long productId;
    private String productName;
    private String sku;
    private Integer totalStock;

    public ProductStockQuantityResponse(Long productId, String productName, String sku, Integer totalStock) {
        this.productId = productId;
        this.productName = productName;
        this.sku = sku;
        this.totalStock = totalStock;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Integer totalStock) {
        this.totalStock = totalStock;
    }
}
