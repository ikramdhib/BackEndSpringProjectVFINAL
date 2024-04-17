package tn.esprit.pidev.RestControllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Repositories.NoteRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.Services.IServiceNote;
import tn.esprit.pidev.entities.Note;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.User;

import java.util.List;
import java.util.logging.Logger;

@Slf4j
@RestController

@RequestMapping("/api/notes")
public class NoteRestController {

    @Autowired
    private IServiceNote iServiceNote;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;


    @PostMapping("/addNote")
    public ResponseEntity<Note> addNote(@RequestBody Note note) {
        Note addedNote = iServiceNote.addNote(note);
        return new ResponseEntity<>(addedNote, HttpStatus.CREATED);
    }

    @PostMapping("/assign/{studentId}/{encadrantId}")
    public ResponseEntity<String> assignNoteToStudent(@PathVariable("studentId") String studentId,
                                                      @PathVariable("encadrantId") String encadrantId,
                                                      @RequestBody Note note) {
        User student = userRepository.findById(studentId).orElse(null);
        User encadrant = userRepository.findById(encadrantId).orElse(null);

        if (student != null && encadrant != null) {
            iServiceNote.assignNoteToStudent(studentId, encadrantId, note);
            return ResponseEntity.ok("Note assigned successfully to student with ID: " + studentId);
        } else {
            return ResponseEntity.badRequest().body("Student or encadrant not found");
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Note>> getNotesByStudentId(@PathVariable("studentId") String studentId) {
        List<Note> notes = iServiceNote.getNotesByStudentId(studentId);
        return ResponseEntity.ok().body(notes);
    }

}
