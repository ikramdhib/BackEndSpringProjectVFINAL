package tn.esprit.pidev.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import tn.esprit.pidev.entities.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends MongoRepository<Tag,String> {
    Tag findByName(String name);
}
