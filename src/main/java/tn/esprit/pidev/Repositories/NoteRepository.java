package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Note;

import java.util.List;

public interface NoteRepository extends MongoRepository<Note,String> {
    List<Note> findByStudentId(String studentId);
}
