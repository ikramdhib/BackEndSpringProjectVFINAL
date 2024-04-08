package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Journal;
import tn.esprit.pidev.entities.Stage;

public interface JournalRepository extends MongoRepository<Journal,String> {
}
