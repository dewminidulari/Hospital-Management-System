package com.example.hospital_manager.service;

import com.example.hospital_manager.dto.AppointmentRequest;
import com.example.hospital_manager.dto.AppointmentResponse;
import com.example.hospital_manager.exception.ApiException;
import com.example.hospital_manager.model.*;
import com.example.hospital_manager.repository.AppointmentRepository;
import com.example.hospital_manager.repository.PatientRepository;
import com.example.hospital_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Patient not found"));
        AppUser doctor = userRepository.findById(request.doctorId())
                .filter(u -> u.getRole() == Role.DOCTOR)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Doctor not found"));

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(request.appointmentDate())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        return toResponse(appointmentRepository.save(appointment));
    }

    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<AppointmentResponse> getAppointmentsByDoctorUsername(String username) {
        AppUser doctor = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Doctor not found"));
        return appointmentRepository.findByDoctorId(doctor.getId()).stream().map(this::toResponse).toList();
    }

    public AppointmentResponse updateStatus(Long id, AppointmentStatus status, String username, boolean isAdmin) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Appointment not found"));

        if (!isAdmin && !appointment.getDoctor().getUsername().equals(username)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You can only update your own appointments");
        }

        appointment.setStatus(status);
        return toResponse(appointmentRepository.save(appointment));
    }

    private AppointmentResponse toResponse(Appointment a) {
        return new AppointmentResponse(
                a.getId(), a.getPatient().getId(), a.getPatient().getName(),
                a.getDoctor().getId(), a.getDoctor().getFullName(),
                a.getAppointmentDate(), a.getStatus().name()
        );
    }
}
