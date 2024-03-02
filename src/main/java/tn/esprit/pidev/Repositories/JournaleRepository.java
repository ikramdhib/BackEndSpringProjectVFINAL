package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Journal;
import tn.esprit.pidev.entities.Offre;

public interface JournaleRepository extends MongoRepository<Journal,String> {
}
