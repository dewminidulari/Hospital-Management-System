package com.example.hospital_manager.controller;

import com.example.hospital_manager.dto.UserResponse;
import com.example.hospital_manager.model.Role;
import com.example.hospital_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StaffController {
    private final UserRepository userRepository;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getFullName(), u.getRole().name()))
                .toList();
    }

    @GetMapping("/doctors")
    public List<UserResponse> getAllDoctors() {
        return userRepository.findByRole(Role.DOCTOR).stream()
                .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getFullName(), u.getRole().name()))
                .toList();
    }
}
