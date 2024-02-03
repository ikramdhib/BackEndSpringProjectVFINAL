package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Forum;

public interface ForumRepository extends MongoRepository<Forum,Integer> {
}
