package tn.esprit.pidev.Repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import tn.esprit.pidev.entities.Offre;
import tn.esprit.pidev.entities.Type;

import java.util.List;

public interface OffreRepository extends MongoRepository<Offre, String> {

    public List<Offre> findOffreByUserIdLike(String id);

    @Query("{ 'user._id': ?0 }")
    List<Offre> findByUser_Id(String userId);

    List<Offre> findByType(Type type);
}
