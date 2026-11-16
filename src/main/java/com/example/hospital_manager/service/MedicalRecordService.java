package com.example.hospital_manager.service;

import com.example.hospital_manager.dto.MedicalRecordRequest;
import com.example.hospital_manager.dto.MedicalRecordResponse;
import com.example.hospital_manager.exception.ApiException;
import com.example.hospital_manager.model.AppUser;
import com.example.hospital_manager.model.MedicalRecord;
import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.repository.MedicalRecordRepository;
import com.example.hospital_manager.repository.PatientRepository;
import com.example.hospital_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class MedicalRecordService {
    private final MedicalRecordRepository recordRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public MedicalRecordResponse createRecord(MedicalRecordRequest request, String doctorUsername) {
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Patient not found"));
        AppUser doctor = userRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Doctor not found"));

        MedicalRecord record = MedicalRecord.builder()
                .patient(patient)
                .doctor(doctor)
                .diagnosis(request.diagnosis())
                .prescription(request.prescription())
                .build();

        MedicalRecord saved = recordRepository.save(record);
        return new MedicalRecordResponse(saved.getId(), patient.getId(), patient.getName(), doctor.getFullName(), saved.getDiagnosis(), saved.getPrescription());
    }

    public List<MedicalRecordResponse> getRecordsByPatient(Long patientId) {
        return recordRepository.findByPatientId(patientId).stream()
                .map(r -> new MedicalRecordResponse(r.getId(), r.getPatient().getId(), r.getPatient().getName(), r.getDoctor().getFullName(), r.getDiagnosis(), r.getPrescription()))
                .toList();
    }
}
