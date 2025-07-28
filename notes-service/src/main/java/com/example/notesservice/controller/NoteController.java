package com.example.notesservice.controller;

import com.example.notesservice.model.Note;
import com.example.notesservice.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService service;
    private static final Logger log = LoggerFactory.getLogger(NoteController.class);

    public NoteController(NoteService service) {
        this.service = service;
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Note>> getByPatient(@PathVariable int patientId) {
        log.info("🔍 Récupération des notes pour le patient ID={}", patientId);
        return ResponseEntity.ok(service.getNotesByPatient(patientId));
    }

    @PostMapping
    public ResponseEntity<Note> create(@RequestBody Note note) {
        log.info("📝 Création d'une nouvelle note: {}", note);
        return ResponseEntity.ok(service.addNote(note));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> update(@PathVariable String id, @RequestBody Note updatedNote) {
        log.info("✏️ Mise à jour de la note ID={} avec données: {}", id, updatedNote);
        Note note = service.updateNote(id, updatedNote);
        if (note != null) {
            log.info("✅ Note mise à jour avec succès: {}", note);
            return ResponseEntity.ok(note);
        } else {
            log.warn("❌ Note non trouvée pour l'ID={}", id);
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("🗑️ Demande de suppression de la note ID={}", id);
        boolean deleted = service.deleteNote(id);
        if (deleted) {
            log.info("✅ Note ID={} supprimée avec succès", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("❌ Impossible de supprimer: note ID={} introuvable", id);
            return ResponseEntity.notFound().build();
        }
    }
}
