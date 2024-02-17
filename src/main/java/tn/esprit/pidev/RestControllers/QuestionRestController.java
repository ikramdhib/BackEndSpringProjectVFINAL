package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Services.QuestionServiceImpl;
import tn.esprit.pidev.entities.Question;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
public class QuestionRestController {
    private QuestionServiceImpl questionService;
    @PostMapping("/addQuestion")
    public ResponseEntity<?> addQuestion(@RequestParam("titre") String titre,
                                         @RequestParam("content") String content,
                                         @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Question question = new Question();
            question.setTitre(titre);
            question.setContent(content);

            if (image != null && !image.isEmpty()) {
                String imageUrl = questionService.saveImage(image);
                question.setImageUrl(imageUrl);  // Assurez-vous que votre entité Question a un champ pour stocker l'URL de l'image
            }

            Question savedQuestion = questionService.addQuestion(question);
            return ResponseEntity.ok(savedQuestion);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite lors de l'enregistrement de la question ou de l'image.");
        }
    }
    @PostMapping("/images")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le fichier ne doit pas être vide");
        }
        try {
            String imageUrl = questionService.saveImage(imageFile); // le serveur reçoit l'image, l'enregistre dans un dossier a l'aide de la méthode saveImage et génère une url
            Map<String, String> response = new HashMap<>();
            response.put("url", imageUrl); // Envoyez l'URL de l'image dans la réponse
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du téléchargement de l'image");
        }
    }
    @GetMapping("/getQuestion")
    public List<Question> getQuestion(){
        return questionService.getQuestion();
    }
    @GetMapping("/getQuestionById/{id}")
    public Question getQuestionById(@PathVariable String id){
        return questionService.getQuestionById(id);
    }
    @DeleteMapping("/deleteQuestion/{id}")
    public void deleteQuestion(@PathVariable String id) {
        questionService.deleteQuestion(id);
    }

}
