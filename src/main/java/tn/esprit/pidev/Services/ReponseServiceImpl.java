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
    public Reponse addReponse(Reponse reponse , String id) {
        User user = userRepository.findById(id).orElse(null);
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
        Map<String,Long> questionCount = userResponses.stream() //démarre un flux stream sul la liste de réponse
                .collect((Collectors.groupingBy(Reponse::getQuestionId,Collectors.counting())));
        // collecter les éléments d'une map clé: Id de la question value : nb de fois que chaque question a été répondue
        List<String> mostAnswerdQuestionId = questionCount.entrySet().stream() // lancer une deuxième flux sur le map questionCount
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) //filtrer le flux des entrées par le nb de réponses par ordre décroissant
                .map(Map.Entry::getKey) //un flux composé des entrées de la map questionCount //key : QuestionId
                .limit(4) // Limiter aux 4 premiers
                .collect(Collectors.toList()); //collecte les identifiants d'une quesstion dans une liste
        //retourne la liste de questions les plus répondues
        List<Question> topQuestions = mostAnswerdQuestionId.stream() //démarrer un flux de stream sur mostAnswerdQuestionId
                .map(questionId -> questionRepository.findById(questionId).orElse(null)) //Pour chaque identifiant de question, recherche la question correspondante dans questionRepository par son identifiant.
                .filter(Objects::nonNull) //filtrer le flux en exclurant les valeurs null
                .collect(Collectors.toList()); //collecter les questions restantes dans une liste

        return topQuestions;
    }

    @Override
    public int nombreReponseByQuestion(String questionId) {
        Question question = questionRepository.findById(questionId).orElse(null);
        List<Reponse> reponses = reponseRepository.findByQuestionId(questionId);
        return reponses.size();
    }

}

















