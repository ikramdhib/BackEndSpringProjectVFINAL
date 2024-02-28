package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Stage;

import java.util.List;
import java.util.Optional;

public interface StageRepository extends MongoRepository<Stage,String> {
    List<Stage> findByUserId(String userId);
}
