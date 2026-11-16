package com.example.hospital_manager.controller;

import com.example.hospital_manager.dto.AppointmentRequest;
import com.example.hospital_manager.dto.AppointmentResponse;
import com.example.hospital_manager.dto.StatusRequest;
import com.example.hospital_manager.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<AppointmentResponse> book(@Valid @RequestBody AppointmentRequest request) {
        return new ResponseEntity<>(appointmentService.bookAppointment(request), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public List<AppointmentResponse> getAppointments(Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return isAdmin ? appointmentService.getAllAppointments() : appointmentService.getAppointmentsByDoctorUsername(auth.getName());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public AppointmentResponse updateStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest request, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return appointmentService.updateStatus(id, request.status(), auth.getName(), isAdmin);
    }
}
