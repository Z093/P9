package com.example.assessmentservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;

@Data
public class PatientDto {
    private int id;
    private String gender;
    private String birthDate; // Format attendu : "YYYY-MM-DD"

    public int getAge() {
        if (birthDate == null || birthDate.isBlank()) return 0;
        try {
            LocalDate birth = LocalDate.parse(birthDate);
            return Period.between(birth, LocalDate.now()).getYears();
        } catch (DateTimeParseException e) {
            // Logger une erreur ici si tu veux
            return 0; // ou -1 pour signaler une erreur de parsing
        }
    }
}
