package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.pidev.Services.IServiceJournal;
import tn.esprit.pidev.entities.Journal;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")

public class JournalRestController {

    public IServiceJournal iServiceJournal;

    @GetMapping("/students/{studentId}/journals")
    public List<Map<String, Object>> getJournalsWithTasksByStudentId(@PathVariable String studentId) {
        return iServiceJournal.getJournalsWithTasksByStudentId(studentId);
    }
}
