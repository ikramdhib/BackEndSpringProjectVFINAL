package tn.esprit.pidev.RestControllers;

import jakarta.ws.rs.Path;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Services.ReponseServiceImpl;
import tn.esprit.pidev.entities.Question;
import tn.esprit.pidev.entities.Reponse;

import java.util.List;
@RestController
@AllArgsConstructor
public class ReponseRestController {
    public ReponseServiceImpl commentaireService;
    @PostMapping("/addReponse/{id}")
    public Reponse addReponse(@RequestBody Reponse reponse ,@PathVariable String id) {
        return commentaireService.addReponse(reponse , id);
    }
    @PutMapping("/updateReponse/{reponseId}")
    public Reponse updateReponse(@PathVariable String reponseId,@RequestBody Reponse reponse){
        return commentaireService.updateReponse(reponseId,reponse);
    }
    @GetMapping("/getAllReponses")
    public List<Reponse> getAllReponses() {
        return commentaireService.getAllReponses();
    }
    @GetMapping("/getResponsesForQuestion/{questionId}")
    public List<Reponse> getResponsesForQuestion(@PathVariable String questionId){
        return commentaireService.getResponsesForQuestion(questionId);
    }
    @DeleteMapping("/deleteReponse/{id}")
    public void deleteCommentaire(@PathVariable String id) {
        commentaireService.deleteReponse(id);
    }
    @GetMapping("/findMostAnsweredQuestionByUser/{userId}")
    public List<Question> findMostAnsweredQuestionsByUser(@PathVariable String userId) {
        return commentaireService.findMostAnsweredQuestionsByUser(userId);
    }
    @GetMapping("/nombreReponseByQuestion/{questionId}")
    public int nombreReponseByQuestion(@PathVariable String questionId) {
        return commentaireService.nombreReponseByQuestion(questionId);
    }

}