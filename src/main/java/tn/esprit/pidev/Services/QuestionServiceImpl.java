package tn.esprit.pidev.Services;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Repositories.QuestionRepository;
import tn.esprit.pidev.Repositories.TagRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.Services.UserServices.CloudinaryService;
import tn.esprit.pidev.Services.UserServices.Pagination.SearchRequest;
import tn.esprit.pidev.Services.UserServices.Pagination.Util.SearchRequestUtil;
import tn.esprit.pidev.entities.Question;
import tn.esprit.pidev.entities.QuestionResponse;
import tn.esprit.pidev.entities.Tag;
import tn.esprit.pidev.entities.User;

import org.springframework.data.domain.Pageable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class QuestionServiceImpl implements IServiceQuestion {
    private QuestionRepository questionRepository;
    private UserRepository userRepository;
    private TagRepository tagRepository;
    private TextRazorService textRazorService;
    private final Path rootLocation = Paths.get("images/");
    private final String apiKey = "AIzaSyB8Mc8MXummb2ZNnkjWEaRnYYoBb8zRrME";

    private CloudinaryService cloudinaryService;

    private RestTemplate restTemplate;
    private final String apiUrl = "https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze?key=" + apiKey;

   /* @Override
    public QuestionResponse addQuestion(Question question , String id) {
        String originalContent = question.getContent();
        double toxicityScore = getToxicityScore(originalContent); // Vous devez implémenter cette méthode
        boolean isContentValid = textRazorService.validateContentForProgramming(question.getContent());

        if (!isContentValid) {
            // Retourner une réponse indiquant que le contenu n'est pas lié à la programmation
            return new QuestionResponse(true, "Le contenu de la question n'est pas lié à la programmation informatique.", null);
        }else if (toxicityScore > 0.1) {
            // Ne pas enregistrer la question et renvoyer une réponse indiquant la présence de contenu toxique
            return new QuestionResponse(true, "Votre contenu contient des mots toxiques", null);
        }

        User user = userRepository.findById(id).orElse(null);
        question.setUser(user);
        processTags(question);
        Question savedQuestion = questionRepository.save(question);
        return new QuestionResponse(false, "Question ajoutée avec succès", savedQuestion);
    }*/
   @Override
   public QuestionResponse addQuestion(Question question , String id) {
       String originalContent = question.getContent();
       double toxicityScore = getToxicityScore(originalContent); // Vous devez implémenter cette méthode
       boolean isContentValid = textRazorService.validateContentForProgramming(question.getContent());

       if (!isContentValid) {
           // Retourner une réponse indiquant que le contenu n'est pas lié à la programmation
           return new QuestionResponse(true, "Le contenu de la question n'est pas lié à la programmation informatique.", null);
       }else if (toxicityScore > 0.1) {
           // Ne pas enregistrer la question et renvoyer une réponse indiquant la présence de contenu toxique
           return new QuestionResponse(true, "Votre contenu contient des mots toxiques", null);
       }

       //consommation R

       String url = "http://localhost:8000/predictTags";

       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);

       Map<String, String> requestBody = new HashMap<>();
       requestBody.put("content", originalContent);

       HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

       ResponseEntity<String[]> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String[].class);

       List<String> tags = new ArrayList<>();
       List<Tag> tagsList = new ArrayList<>();

       if (responseEntity.getStatusCode() == HttpStatus.OK) {
           log.info("it workss");
           tags = Arrays.asList(responseEntity.getBody());
           for(int i=0 ; i<tags.size();i++){
               Tag t = new Tag();
               log.info(tags.get(i));
               t.setName(tags.get(i));
               tagsList.add(t);
           }
       } else {
           // Gérer les erreurs ici, par exemple en lançant une exception
           throw new RuntimeException("Failed to predict tags. Status code: " + responseEntity.getStatusCode());
       }

       User user = userRepository.findById(id).orElse(null);
       question.setUser(user);
       question.setTags(tagsList);
       processTags(question);
       Question savedQuestion = questionRepository.save(question);
       return new QuestionResponse(false, "Question ajoutée avec succès", savedQuestion);
   }


    private void processTags(Question question) {
        if (question.getTags() != null) {
            List<Tag> processedTags = new ArrayList<>();
            for (Tag tag : question.getTags()) {
                Tag existingTag = tagRepository.findByName(tag.getName());
                if (existingTag != null) {
                    existingTag.setUsageCount(existingTag.getUsageCount() + 1);
                    tagRepository.save(existingTag);
                    processedTags.add(existingTag);
                } else {
                    tag.setUsageCount(1);
                    Tag savedTag = tagRepository.save(tag);
                    processedTags.add(savedTag);
                }
            }
            question.setTags(processedTags);
        }
    }

    private double getToxicityScore(String content) {
        // Préparez les en-têtes HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("comment", Collections.singletonMap("text", content));
        requestPayload.put("requestedAttributes", Collections.singletonMap("TOXICITY", new HashMap<>()));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestPayload, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, requestEntity, Map.class);

        double toxicityScore = 0.0;

        // Vérifiez si la réponse est réussie
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();

            // Vérifiez que la réponse contient le score
            if (responseBody.containsKey("attributeScores")) {
                Map<String, Object> attributeScores = (Map<String, Object>) responseBody.get("attributeScores");
                Map<String, Object> toxicity = (Map<String, Object>) attributeScores.get("TOXICITY");
                Map<String, Object> summaryScore = (Map<String, Object>) toxicity.get("summaryScore");
                toxicityScore = (Double) summaryScore.get("value");
            }
        }

        return toxicityScore;

    }


    public String saveImage(MultipartFile imageFile) throws IOException {
        //vérifier si le fichier reçu est vide
     /*   if (imageFile.isEmpty()) {
            throw new IOException("Le fichier reçu est vide");
        }*/
        //récupérer le nom de fichier original du fichier téléchargé
    //    String originalFilename = imageFile.getOriginalFilename();
        //extraire l'extension du fichier a partie de son nom
     //   String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //génère un nom de fichier unique pour l'image + extension
     //   String savedFileName = UUID.randomUUID().toString() + extension;
        //créer un objet path qui représente le chemin d'accèes au fichier de destination sur le serveur
       /* Path destinationFile = this.rootLocation.resolve(Paths.get(savedFileName))
                .normalize().toAbsolutePath();*/
        //vérifie que le chemin de destination est dans le répertoire rootLocation"
    /*    if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
            throw new IOException("Impossible de stocker le fichier en dehors du répertoire courant");
        }*/
        //ouvre un inputStream pour lire le contenu du fichier image et copie le contenu dans le fichier de destination
        //si le fichier existe déjà, il est remplacé
      /*  try (InputStream inputStream = imageFile.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        }*/
        // construire une url ou l'image est accesible

        BufferedImage bi = ImageIO.read(imageFile.getInputStream());

        if (bi == null) {
            throw new IOException("Le fichier reçu est vide");
        }
        Map result = cloudinaryService.upload(imageFile);

        String imageUrl = (String) result.get("url");

        return imageUrl;
    }


    @Override
    public Page<Question> getQuestion(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    @Override
    public Question getQuestionById(String id) {
        return questionRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteQuestion(String id) {
        questionRepository.deleteById(id);
    }



    @PostConstruct // Pour créer le répertoire des images au démarrage si nécessaire
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le dossier pour les images", e);
        }
    }

    @Override
    public List<Question> getAllQuestionForUser(String id) {
         return questionRepository.findByUserId(id);
    }

    @Override
    public Page<Question> getAllQuestionWithTags(String name , Pageable pageable) {
        return questionRepository.findByTagsNameLike(name ,pageable );
    }

}