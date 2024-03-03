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
        //récupération de toute les réponses d'un utilisateur
        List<Reponse> responses = reponseRepository.findByUserId(userId);
        //création d'un HashMap pour garder le nombre de fois d'une question a été répandue
        Map<String, Long> questionCount = new HashMap<>();

        // Compter le nombre de réponses par ID de question
        for (Reponse reponse : responses) {
            //extraire ID de la question a partir de la réponse actuelle
            String questionId = reponse.getQuestionId();
            //Pour chaque réponse, vous vérifiez si questionId est déjà dans la map questionCount.
            // Si ce n'est pas le cas, vous l'ajoutez avec une valeur de 1
            // Si l'ID de la question est déjà présent, vous augmentez le compte actuel de 1.
            if (!questionCount.containsKey(questionId)) {
                questionCount.put(questionId, 1L);
            } else {
                questionCount.put(questionId, questionCount.get(questionId) + 1);
            }
        }

        // Transformer la map en liste pour pouvoir la trier
        List<Map.Entry<String, Long>> entries = new ArrayList<>(questionCount.entrySet());

        // Trier les entrées par le nombre de réponses, en ordre décroissant
        Collections.sort(entries, new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // Récupérer les 4 premiers ID de questions
        List<String> mostAnsweredQuestionId = new ArrayList<>();
        for (int i = 0; i < Math.min(entries.size(), 4); i++) {
            mostAnsweredQuestionId.add(entries.get(i).getKey());
        }

        // Récupérer les questions correspondant aux ID
        //Pour chaque ID de question dans la liste des quatre premiers,
        // vous recherchez la question correspondante dans questionRepository.
        // Si la question existe, vous l'ajoutez à la liste des topQuestions.
        List<Question> topQuestions = new ArrayList<>();
        for (String questionId : mostAnsweredQuestionId) {
            Question question = questionRepository.findById(questionId).orElse(null);
            if (question != null) {
                topQuestions.add(question);
            }
        }

        return topQuestions;
    }


    @Override
    public int nombreReponseByQuestion(String questionId) {
        Question question = questionRepository.findById(questionId).orElse(null);
        List<Reponse> reponses = reponseRepository.findByQuestionId(questionId);
        return reponses.size();
    }

}

















