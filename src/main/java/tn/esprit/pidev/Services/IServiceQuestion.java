package tn.esprit.pidev.Services;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.entities.Question;

import java.io.IOException;
import java.util.List;

public interface IServiceQuestion {
    public Question addQuestion(Question question);
    public List<Question> getQuestion();
    public Question getQuestionById(String id);
    public void deleteQuestion(String id);
}
