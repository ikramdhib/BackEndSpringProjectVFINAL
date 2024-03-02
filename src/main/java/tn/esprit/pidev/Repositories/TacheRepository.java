package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.Tache;
import tn.esprit.pidev.entities.User;

import java.util.List;

public interface TacheRepository extends MongoRepository<Tache,String> {
    List<Tache> findTacheByJournal(String journalId);
    List<Tache> findByJournal_Id(String journalId);
    List<Stage> findByJournal_TachesContains(Tache tache);

}
