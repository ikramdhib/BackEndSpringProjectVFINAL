package tn.esprit.pidev.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.NoteRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.Note;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class NoteServiceImpl implements IServiceNote{

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public Note addNote(Note note) {
        return noteRepository.save(note);
    }

    @Override
    public void assignNoteToStudent(String studentId, String encadrantId, Note note) {
        User student = userRepository.findById(studentId).orElse(null);
        User encadrant = userRepository.findById(encadrantId).orElse(null);

        if (student != null && encadrant != null) {
            note.setStudent(student); // Définir l'étudiant de la note
            note.setEncadrant(encadrant); // Définir l'encadrant de la note
            Note savedNote = noteRepository.save(note);

            // Initialiser la liste des notes de l'étudiant si elle est null
            List<Note> studentNotes = student.getNotes();
            if (studentNotes == null) {
                studentNotes = new ArrayList<>();
            }

            // Ajouter la note à la liste des notes de l'étudiant
            studentNotes.add(note);
            student.setNotes(studentNotes);
            userRepository.save(student);
        } else {
            // Gérer le cas où l'étudiant ou l'encadrant n'est pas trouvé
        }
    }

    @Override
    public List<Note> getNotesByStudentId(String studentId) {
        return noteRepository.findByStudentId(studentId);
    }

}
