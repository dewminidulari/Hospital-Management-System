package com.example.hospital_manager.config;

import com.example.hospital_manager.model.AppUser;
import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.model.Role;
import com.example.hospital_manager.repository.PatientRepository;
import com.example.hospital_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(AppUser.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("System Administrator")
                    .role(Role.ADMIN)
                    .build());

            userRepository.save(AppUser.builder()
                    .username("doctor1")
                    .password(passwordEncoder.encode("doc123"))
                    .fullName("Dr. Perera")
                    .role(Role.DOCTOR)
                    .build());
        }

        if (patientRepository.count() == 0) {
            patientRepository.save(Patient.builder()
                    .name("Kamal Silva")
                    .contactNumber("0771234567")
                    .address("Colombo")
                    .age(35)
                    .gender("Male")
                    .build());
        }
    }
}
