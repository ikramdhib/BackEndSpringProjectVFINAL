package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.CommentoffreRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.Commentoffres;
import tn.esprit.pidev.entities.User;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentaireoffreService {
    @Autowired
    private CommentoffreRepository commentaireRepository;
    private UserRepository userRepository;

    public List<Commentoffres> getAllCommentaires() {
        return commentaireRepository.findAll();
    }

    public Optional<Commentoffres> getCommentaireById(String id) {
        return commentaireRepository.findById(id);
    }

    public Commentoffres addCommentaire(Commentoffres commentaire) {
        return commentaireRepository.save(commentaire);
    }

    public void deleteCommentaireById(String id) {
        commentaireRepository.deleteById(id);
    }

    // Autres méthodes de service selon vos besoins


    public void ajouterEtAffecterAUtilisateurEtOffre(Commentoffres commentoffres) {
        commentaireRepository.save(commentoffres);
    }

    public List<Commentoffres> getCommentsByOffre(String offreId) {
        return commentaireRepository.findByOffreId(offreId);
    }
}
