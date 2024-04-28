package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Demande;

import java.util.List;

public interface DemandeRepo  extends MongoRepository<Demande,String> {

    public List<Demande> findDemandeByOffre_id(String id);

}
