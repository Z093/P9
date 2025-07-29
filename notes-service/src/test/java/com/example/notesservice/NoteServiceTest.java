package com.example.notesservice;

import com.example.notesservice.model.Note;
import com.example.notesservice.repository.NoteRepository;
import com.example.notesservice.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoteServiceTest {

    private NoteRepository repository;
    private NoteService service;

    @BeforeEach
    void setUp() {
        repository = mock(NoteRepository.class);
        service = new NoteService(repository);
    }

    @Test
    void testGetNotesByPatient() {
        int patientId = 1;

        Note note1 = new Note(patientId, "Note 1");
        note1.setId("1");

        Note note2 = new Note(patientId, "Note 2");
        note2.setId("2");

        List<Note> mockNotes = Arrays.asList(note1, note2);
        when(repository.findByPatientId(patientId)).thenReturn(mockNotes);

        List<Note> result = service.getNotesByPatient(patientId);

        assertEquals(2, result.size());
        verify(repository).findByPatientId(patientId);
    }

    @Test
    void testAddNote() {
        Note note = new Note(1, "Nouvelle note");
        Note savedNote = new Note(1, "Nouvelle note");
        savedNote.setId("123");

        when(repository.save(note)).thenReturn(savedNote);

        Note result = service.addNote(note);

        assertEquals("123", result.getId());
        verify(repository).save(note);
    }

    @Test
    void testUpdateNote_WhenNoteExists() {
        String id = "1";
        Note existing = new Note(1, "Ancien contenu");
        existing.setId(id);

        Note update = new Note(2, "Nouveau contenu");

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(Note.class))).thenAnswer(i -> i.getArgument(0));

        Note result = service.updateNote(id, update);

        assertNotNull(result);
        assertEquals("Nouveau contenu", result.getContent());
        assertEquals(2, result.getPatientId());
        verify(repository).save(existing);
    }

    @Test
    void testUpdateNote_WhenNoteDoesNotExist() {
        when(repository.findById("999")).thenReturn(Optional.empty());

        Note result = service.updateNote("999", new Note());

        assertNull(result);
    }

    @Test
    void testDeleteNote_WhenNoteExists() {
        String id = "1";
        Note note = new Note(1, "Contenu");
        note.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(note));

        boolean result = service.deleteNote(id);

        assertTrue(result);
        verify(repository).delete(note);
    }

    @Test
    void testDeleteNote_WhenNoteDoesNotExist() {
        when(repository.findById("404")).thenReturn(Optional.empty());

        boolean result = service.deleteNote("404");

        assertFalse(result);
        verify(repository, never()).delete(any());
    }
}
