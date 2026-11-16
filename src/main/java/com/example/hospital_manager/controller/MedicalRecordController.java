package com.example.hospital_manager.controller;

import com.example.hospital_manager.dto.MedicalRecordRequest;
import com.example.hospital_manager.dto.MedicalRecordResponse;
import com.example.hospital_manager.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class MedicalRecordController {
    private final MedicalRecordService recordService;

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MedicalRecordResponse> writeRecord(@Valid @RequestBody MedicalRecordRequest request, Authentication auth) {
        return new ResponseEntity<>(recordService.createRecord(request, auth.getName()), HttpStatus.CREATED);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public List<MedicalRecordResponse> getPatientRecords(@PathVariable Long patientId) {
        return recordService.getRecordsByPatient(patientId);
    }
}