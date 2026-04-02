package com.leo.inventory_management_system.dto.auth;

import com.leo.inventory_management_system.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(@NotBlank @Email String email, @NotBlank String password, @NotNull Role role){}
