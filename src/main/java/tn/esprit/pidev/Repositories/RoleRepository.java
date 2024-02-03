package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Role;

public interface RoleRepository extends MongoRepository<Role,Integer> {
}
