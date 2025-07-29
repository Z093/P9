package com.example.medilabo;

import com.example.medilabo.model.Patient;
import com.example.medilabo.repository.PatientRepository;
import com.example.medilabo.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PatientServiceTest {

    private PatientRepository repository;
    private PatientService service;

    @BeforeEach
    void setUp() {
        repository = mock(PatientRepository.class);
        service = new PatientService(repository);
    }

    @Test
    void testGetAllPatients() {
        Patient p1 = new Patient("Jean", "Dupont", "1980-01-01", "M", "0600000000", "10 rue A");
        p1.setId(1);
        Patient p2 = new Patient("Marie", "Durand", "1990-01-01", "F", "0611111111", "20 rue B");
        p2.setId(2);

        when(repository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Patient> result = service.getAllPatients();

        assertEquals(2, result.size());
        assertEquals("Jean", result.get(0).getFirstName());
        verify(repository).findAll();
    }

    @Test
    void testGetPatientById_Found() {
        Patient patient = new Patient("Alice", "Martin", "1985-06-15", "F", "0601020304", "5 rue X");
        patient.setId(1);
        when(repository.findById(1)).thenReturn(Optional.of(patient));

        Optional<Patient> result = service.getPatientById(1);

        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getFirstName());
        verify(repository).findById(1);
    }

    @Test
    void testGetPatientById_NotFound() {
        when(repository.findById(999)).thenReturn(Optional.empty());

        Optional<Patient> result = service.getPatientById(999);

        assertFalse(result.isPresent());
        verify(repository).findById(999);
    }

    @Test
    void testCreatePatient() {
        Patient patient = new Patient("Luc", "Robert", "1975-05-20", "M", "0622334455", "123 rue Paris");

        when(repository.save(patient)).thenReturn(patient);

        Patient result = service.createPatient(patient);

        assertNotNull(result);
        assertEquals("Luc", result.getFirstName());
        verify(repository).save(patient);
    }

    @Test
    void testUpdatePatient_Found() {
        Patient existing = new Patient("Paul", "Old", "1970-01-01", "M", "0600000000", "ancienne adresse");
        existing.setId(1);

        Patient update = new Patient("Paul", "New", "1970-01-01", "M", "0600000000", "nouvelle adresse");

        when(repository.findById(1)).thenReturn(Optional.of(existing));
        when(repository.save(any(Patient.class))).thenAnswer(inv -> {
            Patient p = inv.getArgument(0);
            p.setId(1);
            return p;
        });

        Optional<Patient> result = service.updatePatient(1, update);

        assertTrue(result.isPresent());
        assertEquals("New", result.get().getLastName());
        assertEquals("nouvelle adresse", result.get().getAddress());
        verify(repository).save(update);
    }

    @Test
    void testUpdatePatient_NotFound() {
        Patient update = new Patient("Missing", "Patient", "1990-01-01", "F", "0655555555", "inconnue");
        when(repository.findById(1)).thenReturn(Optional.empty());

        Optional<Patient> result = service.updatePatient(1, update);

        assertFalse(result.isPresent());
        verify(repository, never()).save(any());
    }

    @Test
    void testDeletePatient() {
        doNothing().when(repository).deleteById(5);

        service.deletePatient(5);

        verify(repository).deleteById(5);
    }
}
