package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Services.CommentaireServiceImpl;
import tn.esprit.pidev.entities.Commentaire;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
public class CommentaireRestController {
    public CommentaireServiceImpl commentaireService;
    @PostMapping("/addCommentaire")
    public Commentaire addCommentaire(@RequestBody Commentaire commentaire) {
        return commentaireService.addCommentaire(commentaire);
    }
    @GetMapping("/getAllCommentaires")
    public List<Commentaire> getAllCommentaires() {
        return commentaireService.getAllCommentaires();
    }
    @GetMapping("/getResponsesForQuestion/{questionId}")
    public List<Commentaire> getResponsesForQuestion(@PathVariable String questionId){
        return commentaireService.getResponsesForQuestion(questionId);
    }

}
