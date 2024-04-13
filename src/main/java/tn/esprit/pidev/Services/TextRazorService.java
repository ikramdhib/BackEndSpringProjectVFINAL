package tn.esprit.pidev.Services;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
@Service
@Slf4j
public class TextRazorService {
    private static final Logger log = LoggerFactory.getLogger(TextRazorService.class);

    @Value("${textrazor.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean validateContentForProgramming(String content) {
        String url = "https://api.textrazor.com";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("x-textrazor-key", apiKey);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("text", content);
        body.add("extractors", "topics");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, requestEntity, Map.class);
        Map<String, Object> response = responseEntity.getBody();

        // Log the entire response
        //log.info("TextRazor Response: {}", response);

        return response != null && isProgrammingRelated(response);
    }

    private boolean isProgrammingRelated(Map<String, Object> response) {
        // Vérifiez les 'topics' en plus des 'categories'
        List<Map<String, Object>> topics = null;
        if (response.containsKey("response")) {
            Map<String, Object> responseContent = (Map<String, Object>) response.get("response");
            if (responseContent.containsKey("topics")) {
                topics = (List<Map<String, Object>>) responseContent.get("topics");
            }
        }

        // Logique pour vérifier si l'un des sujets ou catégories est lié à la programmation
        if (topics != null) {
            return topics.stream()
                    .anyMatch(topic -> topic.get("label").toString().toLowerCase().contains("programming")
                            || topic.get("label").toString().toLowerCase().contains("computing")
                            || topic.get("label").toString().toLowerCase().contains("software")
                            || topic.get("label").toString().toLowerCase().contains("web development")
                            || topic.get("label").toString().toLowerCase().contains("technology"));
        }

        // Si aucune catégorie ou sujet pertinent n'est trouvé, retournez false
        return false;
    }

}
