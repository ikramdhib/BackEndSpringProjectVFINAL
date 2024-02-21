package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.User;

import java.util.List;

public interface StageRepository extends MongoRepository<Stage,String> {
    List<Stage> findByUser(User user);
    List<Stage> findByUser_Id(String userId);

}
