package tn.esprit.pidev.RestControllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Services.IServiceJournal;
import tn.esprit.pidev.Services.IServiceStage;
import tn.esprit.pidev.entities.Journal;
import tn.esprit.pidev.entities.Stage;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600 , allowCredentials = "true")

@RequestMapping("/api/journals")
public class JournalRestController {

    private final IServiceJournal serviceJournal;

    private final IServiceStage stageService;
    public JournalRestController(IServiceJournal serviceJournal, IServiceStage stageService) {
        this.serviceJournal = serviceJournal;
        this.stageService = stageService;
    }

    @PostMapping("/add/{stageId}")
    public ResponseEntity<?> addJournal(@RequestBody Journal journal, @PathVariable String stageId) {
        try {
            Journal savedJournal = serviceJournal.addJournal(journal, stageId);
            return new ResponseEntity<>(savedJournal, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors d ajout de journal'", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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


}
