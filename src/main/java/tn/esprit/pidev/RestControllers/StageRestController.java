package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.Services.IServiceStage;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.User;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600 , allowCredentials = "true")

@RequestMapping("/api/stages")
public class StageRestController {

    private final IServiceStage stageService;

    @Autowired
    public StageRestController(IServiceStage stageService) {
        this.stageService = stageService;
    }
    @Autowired
    UserRepository userRepository ;
    @PostMapping("/ajouterEtAffecterStageAUtilisateur/{id}")
    public ResponseEntity<String> ajouterEtAffecterStageAUtilisateur(@PathVariable String id , @RequestBody Stage stage) {
        System.out.println("testttt");
        User user = userRepository.findById(id).get() ;
        stage.setUser(user);
        System.out.println("hello stage user "+stage.getUser().getFirstName());
        if (stage.getUser() != null) {
            String userId = stage.getUser().getId();
            stageService.ajouterEtAffecterStageAUtilisateur(stage, userId);
            return ResponseEntity.ok("Stage ajouté et affecté à l'utilisateur avec succès !");
        } else {
            // Gérer le cas où User est null, par exemple, en renvoyant une erreur appropriée.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur associé au stage est null.");
        }
    }

    @PostMapping("/save-demande-stage/{userId}")
    public ResponseEntity<String> saveDemandeStage(@PathVariable String userId, @RequestBody String demandeStageContent) {
        stageService.saveDemandeStage(userId, demandeStageContent);
        return ResponseEntity.ok("Demande de stage enregistrée avec succès.");
    }


    @GetMapping("/user/{userId}")
    public List<Stage> getStagesByUserId(@PathVariable String userId) {
        return stageService.getStagesByUserId(userId);
    }


    @PutMapping("/{stageId}")
    public ResponseEntity<Stage> updateStage(@PathVariable String stageId, @RequestBody Stage updatedStage) {
        Stage updated = stageService.updateStage(stageId, updatedStage);

        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{stageId}")
    public ResponseEntity<?> deleteStage(@PathVariable String stageId) {
        try {
            // Appeler le service pour supprimer le stage
            stageService.deleteStageById(stageId);
            return new ResponseEntity<>("Stage supprimé avec succès", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la suppression du stage", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{stageId}")
    public Stage getStageById(@PathVariable String stageId){
       return stageService.getStageById(stageId);
    }

    @GetMapping("/isJournalAssociated/{stageId}")
    public ResponseEntity<Boolean> isJournalAssociated(@PathVariable String stageId) {
        boolean isAssociated = stageService.isJournalAssociated(stageId);
        return ResponseEntity.ok(isAssociated);
    }


}
