package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.RoleName;
import tn.esprit.pidev.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByLogin(String login);

    User findByLoginLike(String login);

    List<User> findByRole(RoleName roleName);
}