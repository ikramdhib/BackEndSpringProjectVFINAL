package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Services.QuestionServiceImpl;
import tn.esprit.pidev.entities.Question;

import java.io.IOException;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
public class QuestionRestController {
    private QuestionServiceImpl questionService;
    @PostMapping("/addQuestion")
    public Question addQuestion(@RequestBody Question question) {
        return questionService.addQuestion(question);
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
