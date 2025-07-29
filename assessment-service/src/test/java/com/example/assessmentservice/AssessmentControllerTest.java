package com.example.assessmentservice;

import com.example.assessmentservice.controller.AssessmentController;
import com.example.assessmentservice.model.AssessmentResult;
import com.example.assessmentservice.service.AssessmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssessmentController.class)
public class AssessmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssessmentService assessmentService;

    @WithMockUser // simule un utilisateur connecté
    @Test
    void testAssessPatient_returnsAssessmentResult() throws Exception {
        int patientId = 1;
        AssessmentResult result = new AssessmentResult(patientId, "Borderline");

        when(assessmentService.assessRisk(patientId)).thenReturn(result);

        mockMvc.perform(get("/assess/{patientId}", patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(patientId))
                .andExpect(jsonPath("$.riskLevel").value("Borderline"));
    }
}
