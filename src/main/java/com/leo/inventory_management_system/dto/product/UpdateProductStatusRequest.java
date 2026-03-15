package com.leo.inventory_management_system.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class UpdateProductStatusRequest {

    @Schema(description = "Product status: active", example = "false/true")
    @NotNull
    private Boolean active;

    public UpdateProductStatusRequest(){}

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
