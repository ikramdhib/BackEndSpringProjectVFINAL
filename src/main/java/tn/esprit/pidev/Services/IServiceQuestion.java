package tn.esprit.pidev.Services;

import tn.esprit.pidev.entities.Question;

import java.util.List;

public interface IServiceQuestion {
    public Question addQuestion(Question question);
    public List<Question> getQuestion();
    public Question getQuestionById(String id);
    public void deleteQuestion(String id);
}
