package com.example.medilabo;

import com.example.medilabo.controller.PatientController;
import com.example.medilabo.model.Patient;
import com.example.medilabo.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
@AutoConfigureMockMvc(addFilters = false)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

    private Patient patient;

    @BeforeEach
    void setup() {
        patient = new Patient("Jean", "Dupont", "1980-01-01", "M", "0600000000", "10 rue A");
        patient.setId(1);
    }

    @Test
    void testGetAllPatients() throws Exception {
        when(patientService.getAllPatients()).thenReturn(Arrays.asList(patient));

        mockMvc.perform(get("/patient"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jean"));
    }

    @Test
    void testGetById_Found() throws Exception {
        when(patientService.getPatientById(1)).thenReturn(Optional.of(patient));

        mockMvc.perform(get("/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Dupont"));
    }

    @Test
    void testGetById_NotFound() throws Exception {
        when(patientService.getPatientById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/patient/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePatient() throws Exception {
        when(patientService.createPatient(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jean"));
    }

    @Test
    void testUpdatePatient_Found() throws Exception {
        when(patientService.updatePatient(eq(1), any(Patient.class))).thenReturn(Optional.of(patient));

        mockMvc.perform(put("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jean"));
    }

    @Test
    void testUpdatePatient_NotFound() throws Exception {
        when(patientService.updatePatient(eq(999), any(Patient.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/patient/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePatient() throws Exception {
        doNothing().when(patientService).deletePatient(1);

        mockMvc.perform(delete("/patient/1"))
                .andExpect(status().isNoContent());
    }
}
