package com.example.hospital_manager.controller;

import com.example.hospital_manager.model.AppUser;
import com.example.hospital_manager.model.AppointmentStatus;
import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.service.PatientService;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public List<Patient> getPatients(@RequestParam(required = false) String name) {
        return (name != null && !name.isBlank()) ? patientService.searchPatients(name) : patientService.getAllPatients();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
        return new ResponseEntity<>(patientService.createPatient(patient), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}

