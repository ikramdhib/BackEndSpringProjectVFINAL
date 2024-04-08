package tn.esprit.pidev.Services;

import org.springframework.http.ResponseEntity;
import tn.esprit.pidev.entities.Note;
import tn.esprit.pidev.entities.Stage;

import java.util.List;

    public interface IServiceNote {

        public Note addNote(Note note);
        public void assignNoteToStudent(String studentId, String encadrantId, Note note);
        public List<Note> getNotesByStudentId(String studentId);

    }

