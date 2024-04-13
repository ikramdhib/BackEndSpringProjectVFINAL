package tn.esprit.pidev.Services;

import tn.esprit.pidev.entities.Question;
import tn.esprit.pidev.entities.Reponse;

import java.util.List;

public interface IServiceReponse {
    public Reponse addReponse(Reponse reponse);
    public Reponse updateReponse(String reponseId,Reponse reponse);
    public List<Reponse> getAllReponses();
    public List<Reponse> getResponsesForQuestion(String questionId);
    public void deleteReponse(String id);
    public List<Question> findMostAnsweredQuestionsByUser(String userId);
    public int nombreReponseByQuestion(String questionId);
}