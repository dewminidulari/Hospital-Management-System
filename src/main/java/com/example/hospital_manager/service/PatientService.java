package com.example.hospital_manager.service;

import com.example.hospital_manager.exception.ApiException;
import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.repository.AppointmentRepository;
import com.example.hospital_manager.repository.MedicalRecordRepository;
import com.example.hospital_manager.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public List<Patient> searchPatients(String name) {
        return patientRepository.findByNameContainingIgnoreCase(name);
    }

    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Patient not found");
        }
        if (appointmentRepository.existsByPatientId(id) || medicalRecordRepository.existsByPatientId(id)) {
            throw new ApiException(HttpStatus.CONFLICT, "Cannot delete patient with existing appointments or medical records");
        }
        patientRepository.deleteById(id);
    }
}
