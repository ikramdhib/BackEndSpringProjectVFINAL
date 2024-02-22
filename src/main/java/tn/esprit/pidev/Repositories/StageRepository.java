package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Stage;

public interface StageRepository extends MongoRepository<Stage,String> {
    Stage findByUser_id(String userId);
}
