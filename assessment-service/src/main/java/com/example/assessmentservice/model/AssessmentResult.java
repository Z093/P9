package com.example.assessmentservice.model;

public class AssessmentResult {
    private int patientId;
    private String riskLevel;

    // Constructeur vide
    public AssessmentResult() {
    }

    // Constructeur avec tous les champs
    public AssessmentResult(int patientId, String riskLevel) {
        this.patientId = patientId;
        this.riskLevel = riskLevel;
    }

    // Getters
    public int getPatientId() {
        return patientId;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    // Setters
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    // Optionnel : méthode toString
    @Override
    public String toString() {
        return "AssessmentResult{" +
                "patientId=" + patientId +
                ", riskLevel='" + riskLevel + '\'' +
                '}';
    }
}
