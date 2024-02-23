package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import tn.esprit.pidev.entities.Reponse;

import java.util.List;
@CrossOrigin(origins = "*")
public interface ReponseRepository extends MongoRepository<Reponse,String> {
    List<Reponse> findByQuestionId(String questionId);
}
