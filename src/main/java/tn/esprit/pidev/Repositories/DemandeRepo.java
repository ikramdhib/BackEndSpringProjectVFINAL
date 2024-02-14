package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Demande;

public interface DemandeRepo  extends MongoRepository<Demande,String> {

}
