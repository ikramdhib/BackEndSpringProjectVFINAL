package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import tn.esprit.pidev.entities.Historique;

import java.util.List;

@CrossOrigin(origins = "*")
public interface HistoriqueRepository extends MongoRepository<Historique,String> {
    List<Historique> findByUserId(String userId);
}
