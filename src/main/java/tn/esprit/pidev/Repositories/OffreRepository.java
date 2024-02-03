package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Offre;

public interface OffreRepository extends MongoRepository<Offre,Integer> {
}
