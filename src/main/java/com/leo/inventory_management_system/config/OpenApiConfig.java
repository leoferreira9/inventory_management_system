package com.leo.inventory_management_system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI inventoryApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory Management API")
                        .description("API for managing products, stock lots and stock movements")
                        .version("1.0"));
    }
}
