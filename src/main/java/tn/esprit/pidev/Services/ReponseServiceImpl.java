package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
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
    public Reponse updateReponse(String reponseId,Reponse reponse) {
        Reponse reponse1 = reponseRepository.findById(reponseId).orElse(null);
        reponse1.setContent(reponse.getContent());
        return reponseRepository.save(reponse1);
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

    @Override
    public List<Question> findMostAnsweredQuestionByUser(String userId) {
        // Obtenir toutes les réponses de l'utilisateur statique
        List<Reponse> userResponses = reponseRepository.findByUserId(userId);
        // Compter le nombre de réponses par ID de question
        Map<String,Long> questionCount = userResponses.stream()
                .collect((Collectors.groupingBy(Reponse::getQuestionId,Collectors.counting())));
        List<String> mostAnswerdQuestionId = questionCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(4) // Limiter aux 4 premiers
                .collect(Collectors.toList());
        List<Question> topQuestions = mostAnswerdQuestionId.stream()
                .map(questionId -> questionRepository.findById(questionId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return topQuestions;
    }

    @Override
    public int nombreReponseByQuestion(String questionId) {
        Question question = questionRepository.findById(questionId).orElse(null);
        List<Reponse> reponses = reponseRepository.findByQuestionId(questionId);
        return reponses.size();
    }

}

















