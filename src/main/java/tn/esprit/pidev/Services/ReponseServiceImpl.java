package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tn.esprit.pidev.Repositories.QuestionRepository;
import tn.esprit.pidev.Repositories.ReponseRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.Question;
import tn.esprit.pidev.entities.Reponse;
import tn.esprit.pidev.entities.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ReponseServiceImpl implements IServiceReponse {
    private ReponseRepository reponseRepository;
    private UserRepository userRepository;
    private QuestionRepository questionRepository;

    private final String apiKey = "AIzaSyB8Mc8MXummb2ZNnkjWEaRnYYoBb8zRrME";
    private final String apiUrl = "https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze?key=" + apiKey;

    private String filterContent(String content) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("comment", Collections.singletonMap("text", content));
        requestPayload.put("requestedAttributes", Collections.singletonMap("TOXICITY", new HashMap<>()));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestPayload, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, requestEntity, Map.class);

        double toxicityScore = 0.0;
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> attributeScores = (Map<String, Object>) response.getBody().get("attributeScores");
            Map<String, Object> toxicity = (Map<String, Object>) attributeScores.get("TOXICITY");
            Map<String, Object> summaryScore = (Map<String, Object>) toxicity.get("summaryScore");
            toxicityScore = (Double) summaryScore.get("value");
            log.info("Score de toxicité: {}", toxicityScore);
        }

        if (toxicityScore > 0.1) {
            return content.replaceAll("\\S", "*");
        }

        return content;
    }

    @Override
    public Reponse addReponse(Reponse reponse) {
        String originalContent = reponse.getContent();
        String filteredContent = filterContent(originalContent);
        reponse.setContent(filteredContent);
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

















