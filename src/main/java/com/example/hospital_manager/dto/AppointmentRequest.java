package com.example.hospital_manager.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AppointmentRequest(
        @NotNull Long patientId,
        @NotNull Long doctorId,
        @NotNull @Future(message = "Appointment date must be in the future") LocalDateTime appointmentDate
) {
}
