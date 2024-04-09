package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Commentoffres;

import java.util.List;

public interface CommentoffreRepository extends MongoRepository<Commentoffres, String> {
    List<Commentoffres> findByOffreId(String offreId);


}