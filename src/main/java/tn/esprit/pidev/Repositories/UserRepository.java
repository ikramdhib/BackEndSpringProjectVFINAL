package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.User;

public interface UserRepository extends MongoRepository<User,String> {
}
