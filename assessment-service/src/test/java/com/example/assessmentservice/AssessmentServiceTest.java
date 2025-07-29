package com.example.assessmentservice;

import com.example.assessmentservice.dto.NoteDto;
import com.example.assessmentservice.dto.PatientDto;
import com.example.assessmentservice.model.AssessmentResult;
import com.example.assessmentservice.service.AssessmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AssessmentServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AssessmentService assessmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private PatientDto createPatient(int id, String gender, String birthDate) {
        PatientDto patient = new PatientDto();
        patient.setId(id);
        patient.setGender(gender);
        patient.setBirthDate(birthDate);
        return patient;
    }

    private NoteDto createNote(String content) {
        NoteDto note = new NoteDto();
        note.setContent(content);
        return note;
    }

    @Test
    void testAssessRisk_none() {
        PatientDto patient = createPatient(1, "M", "1980-01-01");
        NoteDto[] notes = {
                createNote("Le patient va bien. Aucun symptôme inquiétant.")
        };

        when(restTemplate.getForObject("http://patient-service:8082/patient/1", PatientDto.class)).thenReturn(patient);
        when(restTemplate.getForObject("http://notes-service:8083/notes/patient/1", NoteDto[].class)).thenReturn(notes);

        AssessmentResult result = assessmentService.assessRisk(1);

        assertEquals("None", result.getRiskLevel());
    }

    @Test
    void testAssessRisk_borderline() {
        PatientDto patient = createPatient(2, "F", "1975-03-10"); // Âge > 30
        NoteDto[] notes = {
                createNote("Fumeuse depuis 5 ans."),
                createNote("cholesterol élevé."),
                createNote("Vertiges légers.")
        };

        when(restTemplate.getForObject("http://patient-service:8082/patient/2", PatientDto.class)).thenReturn(patient);
        when(restTemplate.getForObject("http://notes-service:8083/notes/patient/2", NoteDto[].class)).thenReturn(notes);

        AssessmentResult result = assessmentService.assessRisk(2);

        assertEquals("Borderline", result.getRiskLevel());
    }

    @Test
    void testAssessRisk_earlyOnset_maleUnder30() {
        PatientDto patient = createPatient(3, "M", "2000-01-01"); // Âge < 30
        NoteDto[] notes = {
                createNote("hemoglobine a1c élevé."),
                createNote("fumeur."),
                createNote("cholesterol élevé."),
                createNote("vertiges."),
                createNote("anormal.")
        };

        when(restTemplate.getForObject("http://patient-service:8082/patient/3", PatientDto.class)).thenReturn(patient);
        when(restTemplate.getForObject("http://notes-service:8083/notes/patient/3", NoteDto[].class)).thenReturn(notes);

        AssessmentResult result = assessmentService.assessRisk(3);

        assertEquals("Early onset", result.getRiskLevel());
    }

    @Test
    void testAssessRisk_restClientException() {
        when(restTemplate.getForObject(anyString(), eq(PatientDto.class)))
                .thenThrow(new RestClientException("Service not available"));

        AssessmentResult result = assessmentService.assessRisk(99);

        assertEquals("Erreur : Échec de communication avec les microservices", result.getRiskLevel());
    }

    @Test
    void testAssessRisk_nullData() {
        when(restTemplate.getForObject("http://patient-service:8082/patient/5", PatientDto.class)).thenReturn(null);

        AssessmentResult result = assessmentService.assessRisk(5);

        assertEquals("Erreur : Données patient ou notes indisponibles", result.getRiskLevel());
    }
}
