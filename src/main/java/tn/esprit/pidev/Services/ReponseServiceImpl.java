package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.QuestionRepository;
import tn.esprit.pidev.Repositories.ReponseRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.Question;
import tn.esprit.pidev.entities.Reponse;
import tn.esprit.pidev.entities.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReponseServiceImpl implements IServiceReponse {
    private ReponseRepository reponseRepository;
    private UserRepository userRepository;
    private QuestionRepository questionRepository;

    @Override
    public Reponse addReponse(Reponse reponse) {
        User user = userRepository.findById("65d5faf88ecbf72fd4d359f2").orElse(null);
        reponse.setUser(user);
        return reponseRepository.save(reponse);
    }

    @Override
    public List<Reponse> getAllReponses() {
        return reponseRepository.findAll();
    }

    @Override
    public List<Reponse> getResponsesForQuestion(String questionId) {
        return reponseRepository.findByQuestionId(questionId);
    }

    @Override
    public void deleteReponse(String id) {
        reponseRepository.deleteById(id);
    }

}
