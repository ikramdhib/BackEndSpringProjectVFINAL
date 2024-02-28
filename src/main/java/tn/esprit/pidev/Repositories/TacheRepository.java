package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.Tache;

public interface TacheRepository extends MongoRepository<Tache,String> {
}
