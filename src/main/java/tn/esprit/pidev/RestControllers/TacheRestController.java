package tn.esprit.pidev.RestControllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Services.IServiceTache;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.Tache;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600 , allowCredentials = "true")

@RequestMapping("/api/taches")
public class TacheRestController {

    public IServiceTache serviceTache;

    public TacheRestController(IServiceTache serviceTache) {

        this.serviceTache = serviceTache;
    }

    @PostMapping("/addWithJournal/{journalId}")
    public Tache addTacheWithJournal(@RequestBody Tache tache, @PathVariable String journalId) {
        return serviceTache.addTacheWithJournal(tache, journalId);
    }

    @GetMapping("/tachesByStage/{stageId}")
    public ResponseEntity<List<Tache>> getTachesByStage(@PathVariable String stageId) {
        try {
            List<Tache> taches = serviceTache.getTachesByStageId(stageId);
            System.out.println("hello stage user "+taches);
            return new ResponseEntity<>(taches, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tachesByJournal/{journalId}")
    public ResponseEntity<List<Tache>> getTachesByJournal(@PathVariable String journalId) {
        try {
            List<Tache> taches = serviceTache.getTachesByJournalId(journalId);
            return new ResponseEntity<>(taches, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{tacheId}")
    public ResponseEntity<Tache> updateTache(@PathVariable String tacheId, @RequestBody Tache updatedTache) {
        Tache updated = serviceTache.updateTache(tacheId, updatedTache);

        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
