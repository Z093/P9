package com.example.medilabo.repository;

import com.example.medilabo.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PatientRepository extends JpaRepository<Patient, Integer> {
}


