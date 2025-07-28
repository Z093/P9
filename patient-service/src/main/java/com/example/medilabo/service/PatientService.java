package com.example.medilabo.service;

import com.example.medilabo.model.Patient;
import com.example.medilabo.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(int id) {
        return patientRepository.findById(id);
    }

    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Optional<Patient> updatePatient(int id, Patient updatedPatient) {
        return patientRepository.findById(id)
                .map(existing -> {
                    updatedPatient.setId(existing.getId());
                    return patientRepository.save(updatedPatient);
                });
    }

    public void deletePatient(int id) {
        patientRepository.deleteById(id);
    }
}
