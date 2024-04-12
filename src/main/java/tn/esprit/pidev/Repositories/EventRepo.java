package tn.esprit.pidev.Repositories;

import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Event;

public interface EventRepo extends MongoRepository<Event,String> {
}