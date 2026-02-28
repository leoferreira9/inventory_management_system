package com.leo.inventory_management_system.dto.product;

import jakarta.validation.constraints.NotNull;

public class UpdateProductStatusRequest {

    @NotNull
    private Boolean active;

    public UpdateProductStatusRequest(){}

    public Boolean getActive() {
        return active;
    }
}
