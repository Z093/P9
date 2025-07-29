package com.example.assessmentservice.dto;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;

public class PatientDto {
    private int id;
    private String gender;
    private String birthDate; // Format attendu : "YYYY-MM-DD"

    public PatientDto() {
    }

    public PatientDto(int id, String gender, String birthDate) {
        this.id = id;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getAge() {
        if (birthDate == null || birthDate.isBlank()) return 0;
        try {
            LocalDate birth = LocalDate.parse(birthDate);
            return Period.between(birth, LocalDate.now()).getYears();
        } catch (DateTimeParseException e) {
            return 0; // ou -1 si tu veux signaler une erreur
        }
    }
}
