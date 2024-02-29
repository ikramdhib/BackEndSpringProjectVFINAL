package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import tn.esprit.pidev.entities.Tag;

import java.util.List;

public interface TagRepository extends MongoRepository<Tag,String> {
    List<Tag> findByNameContainingIgnoreCase(String name);
}
