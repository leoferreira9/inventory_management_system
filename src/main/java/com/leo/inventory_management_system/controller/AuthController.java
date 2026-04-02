package com.leo.inventory_management_system.controller;

import com.leo.inventory_management_system.dto.auth.AuthRequest;
import com.leo.inventory_management_system.dto.auth.AuthResponse;
import com.leo.inventory_management_system.dto.auth.RegisterRequest;
import com.leo.inventory_management_system.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth controller", description = "Endpoints for authentication")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @Operation(summary = "Authenticate user and return JWT token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Register a new User")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered"),
            @ApiResponse(responseCode = "409", description = "Email already in use")
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register (@Valid @RequestBody RegisterRequest request){
        authService.register(request);
        return ResponseEntity.status(201).build();
    }
}
