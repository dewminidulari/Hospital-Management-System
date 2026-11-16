package com.example.hospital_manager.dto;

public record MedicalRecordResponse(
        Long id,
        Long patientId,
        String patientName,
        String doctorName,
        String diagnosis,
        String prescription
) {}
