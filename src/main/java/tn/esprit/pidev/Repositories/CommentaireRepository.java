package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Commentaire;

public interface CommentaireRepository extends MongoRepository<Commentaire,Integer> {
}
