package com.example.hospital_manager.service;

import com.example.hospital_manager.dto.AuthResponse;
import com.example.hospital_manager.dto.LoginRequest;
import com.example.hospital_manager.dto.RegisterRequest;
import com.example.hospital_manager.dto.UserResponse;
import com.example.hospital_manager.model.AppUser;
import com.example.hospital_manager.repository.UserRepository;
import com.example.hospital_manager.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("Username already exists!");
        }

        AppUser user = AppUser.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .role(request.role())
                .build();

        AppUser savedUser = userRepository.save(user);
        return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getFullName(), savedUser.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        AppUser user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                        .password(user.getPassword())
                        .authorities("ROLE_" + user.getRole().name())
                        .build(),
                user.getRole().name()
        );

        return new AuthResponse(token, "Bearer", 3600000, user.getUsername(), user.getFullName(), user.getRole().name());
    }
}