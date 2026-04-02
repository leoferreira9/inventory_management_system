package com.leo.inventory_management_system.service;

import com.leo.inventory_management_system.dto.auth.AuthRequest;
import com.leo.inventory_management_system.dto.auth.AuthResponse;
import com.leo.inventory_management_system.dto.auth.RegisterRequest;
import com.leo.inventory_management_system.entity.User;
import com.leo.inventory_management_system.exception.DuplicatedData;
import com.leo.inventory_management_system.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public AuthResponse login(AuthRequest request){
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = tokenService.generateToken(user);
        return new AuthResponse(token);
    }

    public void register(RegisterRequest request){
        if(userRepository.findByEmail(request.email()).isPresent()){
            throw new DuplicatedData("Email already in use");
        }

        String hashedPassword = passwordEncoder.encode(request.password());
        User user = new User(request.email(), hashedPassword, request.role());
        userRepository.save(user);
    }
}
