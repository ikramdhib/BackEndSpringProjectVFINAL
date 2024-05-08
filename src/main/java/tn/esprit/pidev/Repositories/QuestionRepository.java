package tn.esprit.pidev.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pidev.entities.Question;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionRepository extends MongoRepository<Question,String> {
    Page<Question> findAll(Pageable pageable);

    List<Question> findByUserId(String id);

    Page<Question> findByTagsNameLike(String name , Pageable pageable);
}
