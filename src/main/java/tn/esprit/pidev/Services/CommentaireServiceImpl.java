package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.CommentaireRepository;
import tn.esprit.pidev.entities.Commentaire;

import java.util.List;
@Service
@AllArgsConstructor
public class CommentaireServiceImpl implements IServiceCommentaire{
    private CommentaireRepository commentaireRepository;

    @Override
    public Commentaire addCommentaire(Commentaire commentaire) {
        return commentaireRepository.save(commentaire);
    }

    @Override
    public List<Commentaire> getAllCommentaires() {
        return commentaireRepository.findAll();
    }

    @Override
    public List<Commentaire> getResponsesForQuestion(String questionId) {
        return commentaireRepository.findByQuestionId(questionId);
    }
}
