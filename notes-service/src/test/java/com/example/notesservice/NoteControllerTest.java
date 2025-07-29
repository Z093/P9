package com.example.notesservice;

import com.example.notesservice.controller.NoteController;
import com.example.notesservice.model.Note;
import com.example.notesservice.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
@AutoConfigureMockMvc(addFilters = false)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Note note;

    @BeforeEach
    void setup() {
        note = new Note(1, "Contenu test");
        note.setId("abc123");
    }

    @Test
    void testGetNotesByPatient() throws Exception {
        List<Note> notes = Arrays.asList(note);

        when(service.getNotesByPatient(1)).thenReturn(notes);

        mockMvc.perform(get("/notes/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("abc123"))
                .andExpect(jsonPath("$[0].patientId").value(1))
                .andExpect(jsonPath("$[0].content").value("Contenu test"));
    }

    @Test
    void testCreateNote() throws Exception {
        when(service.addNote(any(Note.class))).thenReturn(note);

        mockMvc.perform(post("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("abc123"))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.content").value("Contenu test"));
    }

    @Test
    void testUpdateNote_Found() throws Exception {
        when(service.updateNote(eq("abc123"), any(Note.class))).thenReturn(note);

        mockMvc.perform(put("/notes/abc123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("abc123"))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.content").value("Contenu test"));
    }

    @Test
    void testUpdateNote_NotFound() throws Exception {
        when(service.updateNote(eq("notfound"), any(Note.class))).thenReturn(null);

        mockMvc.perform(put("/notes/notfound")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteNote_Success() throws Exception {
        when(service.deleteNote("abc123")).thenReturn(true);

        mockMvc.perform(delete("/notes/abc123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteNote_NotFound() throws Exception {
        when(service.deleteNote("xyz")).thenReturn(false);

        mockMvc.perform(delete("/notes/xyz"))
                .andExpect(status().isNotFound());
    }
}
