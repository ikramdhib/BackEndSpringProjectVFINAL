package tn.esprit.pidev.Services;

import tn.esprit.pidev.entities.Commentaire;

import java.util.List;

public interface IServiceCommentaire {
    public Commentaire addCommentaire(Commentaire commentaire);
    public List<Commentaire> getAllCommentaires();
    public List<Commentaire> getResponsesForQuestion(String questionId);
}
