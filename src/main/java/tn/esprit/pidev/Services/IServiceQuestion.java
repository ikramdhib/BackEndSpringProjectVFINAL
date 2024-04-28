package tn.esprit.pidev.Services;


import org.springframework.data.domain.Page;
import tn.esprit.pidev.Services.UserServices.Pagination.SearchRequest;
import tn.esprit.pidev.entities.Question;
import tn.esprit.pidev.entities.QuestionResponse;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IServiceQuestion {
    public QuestionResponse addQuestion(Question question,String id);
    public Page<Question> getQuestion(Pageable pageable);
    public Question getQuestionById(String id);
    public void deleteQuestion(String id);


    public List<Question> getAllQuestionForUser(String id );
}