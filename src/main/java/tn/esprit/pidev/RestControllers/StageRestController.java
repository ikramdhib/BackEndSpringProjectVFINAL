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


}
