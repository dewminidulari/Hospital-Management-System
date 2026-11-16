package com.example.hospital_manager.dto;

import com.example.hospital_manager.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank String password,
        String fullName,
        @NotNull Role role
) {
}
