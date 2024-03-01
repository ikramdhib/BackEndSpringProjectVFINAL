package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.Tache;

import java.util.List;

public interface TacheRepository extends MongoRepository<Tache,String> {
    List<Tache> findByJournal_Stage_Id(String stageId);
    List<Tache> findByJournal_Id(String journalId);
}
