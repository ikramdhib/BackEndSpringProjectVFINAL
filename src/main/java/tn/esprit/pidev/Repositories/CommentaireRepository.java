package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import tn.esprit.pidev.entities.Commentaire;

import java.util.List;
@CrossOrigin(origins = "*")
public interface CommentaireRepository extends MongoRepository<Commentaire,String> {
    List<Commentaire> findByQuestionId(String questionId);
}
