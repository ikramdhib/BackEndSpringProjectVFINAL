package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Reclamation;

public interface ReclamationRepository extends MongoRepository<Reclamation,Integer> {
}
