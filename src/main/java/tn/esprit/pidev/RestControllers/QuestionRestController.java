package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Services.QuestionServiceImpl;
import tn.esprit.pidev.Services.TextRazorService;
import tn.esprit.pidev.entities.Question;
import tn.esprit.pidev.entities.QuestionResponse;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@AllArgsConstructor
public class QuestionRestController {
    private QuestionServiceImpl questionService;
    private TextRazorService textRazorService;

    @PostMapping("/addQuestion/{id}")
    public ResponseEntity<QuestionResponse> addQuestion(@RequestBody Question question ,@PathVariable String id) {

        // Si le contenu est validé, procédez à l'ajout de la question et retournez la réponse
        QuestionResponse response = questionService.addQuestion(question , id);
        // Retournez la réponse dans une ResponseEntity avec le statut HTTP OK
        return ResponseEntity.ok(response);
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
    public Page<Question> getQuestions(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return questionService.getQuestion(pageable);
    }

    @GetMapping("/getQuestionwithTags/{name}")
    public Page<Question> getQuestionsWithTags( @PathVariable String name ,@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size
                                               ) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("name====",name);
        return  questionService.getAllQuestionWithTags(name,pageable);
    }

    @GetMapping("/getQuestionById/{id}")
    public Question getQuestionById(@PathVariable String id){
        return questionService.getQuestionById(id);
    }
    @DeleteMapping("/deleteQuestion/{id}")
    public void deleteQuestion(@PathVariable String id) {
        questionService.deleteQuestion(id);
    }
    @PostMapping("/update-clusters")
    public ResponseEntity<?> updateClusters(@RequestBody Map<String, String> payload) {
        String csvFilePath = payload.get("csvFilePath");
        // Utilisez le chemin pour lire le fichier CSV et mettre à jour MongoDB
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getQuetionsForUser/{id}")
    public ResponseEntity<?> getAllQuestionForUser(@PathVariable String id){
        List<Question> questions = questionService.getAllQuestionForUser(id);
        return  ResponseEntity.status(HttpStatus.OK).body(questions);
    }

}