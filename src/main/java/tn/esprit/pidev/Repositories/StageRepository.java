package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import tn.esprit.pidev.entities.Journal;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.Tache;
import tn.esprit.pidev.entities.User;

import java.util.List;
import java.util.Optional;

public interface StageRepository extends MongoRepository<Stage,String> {
    List<Stage> findByUser(User user);
    List<Stage> findByUser_Id(String userId);
    List<Stage> findByEncadrant(User encadrant);
    Stage findByUserAndEncadrant(User user, User encadrant);
    List<Stage> findByUserId(String userId);
    Optional<Stage> findById(String id);
    List<Stage> findByEncadrantId(String encadrantId);
    List<Stage> findByJournal_TachesContains(Tache tache);
    List<Stage> findByJournal(Journal journal);

    @Query(value = "{ 'user' : { $ne : null } }")
    List<Stage> findStagesWithUsers();




}
