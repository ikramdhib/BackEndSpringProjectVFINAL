package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Services.IServiceStage;
import tn.esprit.pidev.entities.Role;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.User;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class StageRestController {
        @Autowired
        private IServiceStage iServiceStage;
        @GetMapping("/userNames")
        public Map<String, String> getAllStagesWithUserNames() {
                return iServiceStage.getAllStagesWithUserNames();
        }
        @PostMapping("/sendEmailToEncadrant/{stageId}")
        public String sendEmailToEncadrant(@PathVariable String stageId) {
                try {
                        iServiceStage.sendEmailToEncadrant(stageId);
                        iServiceStage.updateEncadrantInfoAndRemoveFromStage(stageId); // Appel de la méthode pour mettre à jour l'utilisateur et supprimer les informations de l'encadrant de la table Stage
                        return "E-mail envoyé à l'encadrant avec succès.";
                } catch (Exception e) {
                        e.printStackTrace();
                        return "Une erreur s'est produite lors de l'envoi de l'e-mail à l'encadrant.";
                }
        }
        @PostMapping("/sendEmailToStudent/{stageId}")
        public String sendEmailToStudent(@PathVariable String stageId) {
                try {
                        iServiceStage.sendEmailToStudent(stageId);
                        return "E-mail envoyé à l'étudiant avec succès.";
                } catch (Exception e) {
                        e.printStackTrace();
                        return "Une erreur s'est produite lors de l'envoi de l'e-mail à l'étudiant.";
                }

}
        @GetMapping("/studentsByEncadrant/{encadrantId}")
        public List<User> getStudentsByEncadrantId(@PathVariable String encadrantId) {
                return iServiceStage.getStudentsByEncadrantId(encadrantId);
        }

}
