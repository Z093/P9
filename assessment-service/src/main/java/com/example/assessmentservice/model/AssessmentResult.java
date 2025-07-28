package com.example.assessmentservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssessmentResult {
    private int patientId;
    private String riskLevel;
}


