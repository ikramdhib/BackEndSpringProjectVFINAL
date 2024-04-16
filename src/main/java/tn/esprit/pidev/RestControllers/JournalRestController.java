package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Services.IServiceJournal;
import tn.esprit.pidev.Services.IServiceStage;
import tn.esprit.pidev.Services.JournalServiceImpl;
import tn.esprit.pidev.entities.Journal;
import tn.esprit.pidev.entities.Stage;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Slf4j
public class JournalRestController {

    public IServiceJournal iServiceJournal;
    public JournalServiceImpl journalService;
    public IServiceStage stageService;


    @GetMapping("/students/{studentId}/journals")
    public List<Map<String, Object>> getJournalsWithTasksByStudentId(@PathVariable String studentId) {
        return iServiceJournal.getJournalsWithTasksByStudentId(studentId);
    }
    @PostMapping("/add/{stageId}")
    public ResponseEntity<?> addJournal(@RequestBody Journal journal, @PathVariable String stageId) {
       //try {
            Journal savedJournal = journalService.addJournal(journal, stageId);
            return new ResponseEntity<>(savedJournal, HttpStatus.CREATED);
      /*  } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors d ajout de journal'", HttpStatus.INTERNAL_SERVER_ERROR);
        }*/
    }

    @GetMapping("/check/{stageId}")
    public ResponseEntity<Boolean> checkIfJournalExists(@PathVariable String stageId) {
        Stage stage = stageService.getStageById(stageId);
        if (stage != null && stage.getJournal() != null) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/{journalId}")
    public Journal getJournal(@PathVariable String journalId){
        return iServiceJournal.getJournal(journalId);
    }

}
