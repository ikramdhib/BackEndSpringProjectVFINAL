package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Question;

public interface QuestionRepository extends MongoRepository<Question,String> {
}
