package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Services.IServiceStage;
import tn.esprit.pidev.Services.ITacheService;
import tn.esprit.pidev.Services.UserServices.IServiceUser;
import tn.esprit.pidev.Services.IServiceTache;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.Tache;

import java.util.List;

@RestController
@AllArgsConstructor
public class TacheRestController {
    private ITacheService iTacheService;
    private IServiceUser iUserService;
    private IServiceStage iServiceStage;

    public IServiceTache serviceTache;

    @GetMapping("/userrrrrrr/{userId}")
    public ResponseEntity<List<Tache>> getTasksForStudent(@PathVariable String userId) {
        List<Tache> tasks = iTacheService.getTasksForUser(userId);
        if (tasks != null && !tasks.isEmpty()) {
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }}
    /////
    @GetMapping("/tachesByJournal/{journalId}")
    public ResponseEntity<List<Tache>> getTachesByJournal(@PathVariable String journalId) {
        try {
            List<Tache> taches = iTacheService.getTachesByJournalId(journalId);
            return new ResponseEntity<>(taches, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/tachesByEtudiant/{etudiantId}")
    public ResponseEntity<List<Tache>> getTachesByEtudiantId(@PathVariable String etudiantId) {
        try {
            List<Stage> stages = iServiceStage.getStagesByUserId(etudiantId);
            if (stages.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Aucun stage trouvé pour cet étudiant
            }
            // Récupérer le premier stage de l'étudiant (vous pouvez ajuster la logique si nécessaire)
            String journalId = stages.get(0).getJournal().getId();
            List<Tache> taches = iTacheService.getTachesByJournalId(journalId);
            return new ResponseEntity<>(taches, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @PostMapping("/taches/{tacheId}/valider")
    public ResponseEntity<String> validateTask(@PathVariable String tacheId) {
        try {
            iTacheService.validateTask(tacheId);
            return new ResponseEntity<>("Tâche validée avec succès", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/taches/{tacheId}/rejeter/{rejectionReason}")
    public ResponseEntity<String> rejectTask(@PathVariable String tacheId, @PathVariable String rejectionReason) {
        try {
            iTacheService.rejectTask(tacheId, rejectionReason);
            return new ResponseEntity<>("Tâche rejetée avec succès", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateTache/{tacheId}")
    public ResponseEntity<Tache> updateTache(@PathVariable String tacheId, @RequestBody Tache updatedTache) {
        Tache updated = serviceTache.updateTache(tacheId, updatedTache);

        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/deleteTache/{tacheId}")
    public ResponseEntity<?> deleteTache(@PathVariable String tacheId) {
        try {
            // Appeler le service pour supprimer le stage
            serviceTache.deleteTacheById(tacheId);
            return new ResponseEntity<>("Tache supprimé avec succès", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la suppression du tache", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
