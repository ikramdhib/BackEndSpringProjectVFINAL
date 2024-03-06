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
    public List<Question> findMostAnsweredQuestionsByUser(String userId) {
        List<Reponse> responses = reponseRepository.findByUserId(userId);
        Map<String, Long> questionCount = new HashMap<>();

        // Compter le nombre de réponses par ID de question
        for (Reponse reponse : responses) {
            String questionId = reponse.getQuestionId();
            if (!questionCount.containsKey(questionId)) {
                questionCount.put(questionId, 1L);
            } else {
                questionCount.put(questionId, questionCount.get(questionId) + 1);
            }
        }
        List<Map.Entry<String, Long>> entries = new ArrayList<>(questionCount.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        List<String> mostAnsweredQuestionId = new ArrayList<>();
        for (int i = 0; i < Math.min(entries.size(), 4); i++) {
            mostAnsweredQuestionId.add(entries.get(i).getKey());
        }
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
        List<Reponse> reponses = reponseRepository.findByQuestionId(questionId);
        return reponses.size();
    }

}

















