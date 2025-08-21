package com.example.assessmentservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;

public class PatientDto {
    private int id;
    private String firstName;
    private String lastName;
    private String gender;
    private String address;
    private String phoneNumber;

    @JsonProperty("dateOfBirth") // Mapping explicite avec le JSON
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // Format de date
    private String birthDate;

    // Constructeurs
    public PatientDto() {}

    // Constructeur complet pour les tests
    public PatientDto(int id, String firstName, String lastName,
                      String gender, String birthDate, String address,
                      String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Getters & Setters
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getBirthDate() { return birthDate; }

    public void setId(int id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setGender(String gender) { this.gender = gender; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    // Méthode critique : Calcul de l'âge
    public int getAge() {
        if (birthDate == null || birthDate.isBlank()) {
            throw new IllegalStateException("La date de naissance est requise");
        }
        try {
            LocalDate birth = LocalDate.parse(birthDate);
            return Period.between(birth, LocalDate.now()).getYears();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Format de date invalide. Utiliser YYYY-MM-DD");
        }
    }

    // Pour le débogage
    @Override
    public String toString() {
        return String.format(
                "Patient[id=%d, gender=%s, birthDate=%s, age=%d]",
                id, gender, birthDate, getAge()
        );
    }
}
