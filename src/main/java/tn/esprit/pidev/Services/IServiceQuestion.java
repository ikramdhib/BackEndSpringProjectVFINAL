package tn.esprit.pidev.Services;


import org.springframework.data.domain.Page;
import tn.esprit.pidev.entities.Question;
import tn.esprit.pidev.entities.QuestionResponse;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IServiceQuestion {
    public QuestionResponse addQuestion(Question question);
    public Page<Question> getQuestion(Pageable pageable);
    public Question getQuestionById(String id);
    public void deleteQuestion(String id);
    public void updateQuestionCluters(String csvFilePath);
}
