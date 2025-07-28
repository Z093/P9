package com.example.assessmentservice.service;

import com.example.assessmentservice.dto.NoteDto;
import com.example.assessmentservice.dto.PatientDto;
import com.example.assessmentservice.model.AssessmentResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.Normalizer;
import java.util.List;

@Service
public class AssessmentService {

    @Autowired
    private RestTemplate restTemplate;

    private static final List<String> TRIGGER_TERMS = List.of(
            "hemoglobine a1c", "microalbumine", "taille", "poids", "fumeur", "fumeuse",
            "anormal", "cholesterol", "vertiges", "rechute", "reaction", "anticorps"
    );

    public AssessmentResult assessRisk(int patientId) {
        try {
            String patientUrl = "http://patient-service:8082/patient/" + patientId;
            String notesUrl = "http://notes-service:8083/notes/patient/" + patientId;

            PatientDto patient = restTemplate.getForObject(patientUrl, PatientDto.class);
            NoteDto[] notes = restTemplate.getForObject(notesUrl, NoteDto[].class);

            if (patient == null || notes == null) {
                return new AssessmentResult(patientId, "Erreur : Données patient ou notes indisponibles");
            }

            int triggerCount = countTriggers(notes);
            String risk = evaluateRisk(patient, triggerCount);

            return new AssessmentResult(patientId, risk);

        } catch (RestClientException e) {
            return new AssessmentResult(patientId, "Erreur : Échec de communication avec les microservices");
        }
    }

    private int countTriggers(NoteDto[] notes) {
        int count = 0;
        for (NoteDto note : notes) {
            if (note.getContent() != null) {
                String normalizedContent = normalize(note.getContent());
                for (String term : TRIGGER_TERMS) {
                    if (normalizedContent.contains(term)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private String evaluateRisk(PatientDto patient, int count) {
        int age = patient.getAge();
        String gender = patient.getGender();

        if (count == 0) return "None";
        if (count >= 2 && count <= 5 && age > 30) return "Borderline";

        if (age <= 30) {
            if ("M".equalsIgnoreCase(gender)) {
                if (count >= 3 && count < 5) return "In Danger";
                if (count >= 5) return "Early onset";
            } else {
                if (count >= 4 && count < 7) return "In Danger";
                if (count >= 7) return "Early onset";
            }
        } else {
            if (count >= 6 && count < 8) return "In Danger";
            if (count >= 8) return "Early onset";
        }

        return "None";
    }


    // Supprime les accents et met en minuscule
    private String normalize(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "") // Enlève les diacritiques
                .toLowerCase();
    }
}
