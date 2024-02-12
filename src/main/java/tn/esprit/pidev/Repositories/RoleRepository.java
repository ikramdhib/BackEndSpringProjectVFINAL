package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Role;
import tn.esprit.pidev.entities.RoleName;

public interface RoleRepository extends MongoRepository<Role,Integer> {
    Role findByRoleName(RoleName roleName);
}
