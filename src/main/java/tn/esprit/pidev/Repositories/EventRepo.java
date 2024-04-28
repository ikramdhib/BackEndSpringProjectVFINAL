package tn.esprit.pidev.Repositories;

import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import tn.esprit.pidev.entities.Event;

import java.util.List;

public interface EventRepo extends MongoRepository<Event,String> {
    @Query("{ 'user._id': ?0 }")
    List<Event> findByUser_Id(String userId);
}