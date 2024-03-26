package tn.esprit.pidev.Services;


import tn.esprit.pidev.entities.Question;
import tn.esprit.pidev.entities.QuestionResponse;

import java.util.List;

public interface IServiceQuestion {
    public QuestionResponse addQuestion(Question question);
    public List<Question> getQuestion();
    public Question getQuestionById(String id);
    public void deleteQuestion(String id);
}
