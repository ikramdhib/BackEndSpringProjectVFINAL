package tn.esprit.pidev.Services;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Repositories.QuestionRepository;
import tn.esprit.pidev.Repositories.TagRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.Question;
import tn.esprit.pidev.entities.Tag;
import tn.esprit.pidev.entities.User;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuestionServiceImpl implements IServiceQuestion {
    private QuestionRepository questionRepository;
    private UserRepository userRepository;
    private TagRepository tagRepository;
    private final Path rootLocation = Paths.get("images/");

    @Override
    public Question addQuestion(Question question) {
        User user = userRepository.findById("65d5faf88ecbf72fd4d359f2").orElse(null);
        question.setUser(user);

        if (question.getTags() != null) {
            List<Tag> processedTags = new ArrayList<>(); // Créez une liste pour stocker les tags traités

            for (Tag tag : question.getTags()) { // Bouclez sur les tags existants de la question
                Tag existingTag = tagRepository.findByName(tag.getName()); // Recherchez le tag par son nom

                if (existingTag != null) { // Si le tag existe déjà
                    existingTag.setUsageCount(existingTag.getUsageCount() + 1); // Incrémentez le compteur d'utilisation
                    tagRepository.save(existingTag); // Enregistrez le tag existant mis à jour
                    processedTags.add(existingTag); // Ajoutez le tag existant à la liste des tags traités
                } else { // Si le tag n'existe pas
                    tag.setUsageCount(1); // Initialisez le compteur d'utilisation
                    Tag savedTag = tagRepository.save(tag); // Enregistrez le nouveau tag
                    processedTags.add(savedTag); // Ajoutez le nouveau tag à la liste des tags traités
                }
            }

            question.setTags(processedTags); // Mettez à jour les tags de la question avec la liste traitée
        }

        return questionRepository.save(question); // Enregistrez la question dans le repository
    }


    public String saveImage(MultipartFile imageFile) throws IOException {
        //vérifier si le fichier reçu est vide
        if (imageFile.isEmpty()) {
            throw new IOException("Le fichier reçu est vide");
        }
        //récupérer le nom de fichier original du fichier téléchargé
        String originalFilename = imageFile.getOriginalFilename();
        //extraire l'extension du fichier a partie de son nom
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //génère un nom de fichier unique pour l'image + extension
        String savedFileName = UUID.randomUUID().toString() + extension;
        //créer un objet path qui représente le chemin d'accèes au fichier de destination sur le serveur
        Path destinationFile = this.rootLocation.resolve(Paths.get(savedFileName))
                .normalize().toAbsolutePath();
        //vérifie que le chemin de destination est dant le répertoire rootLocation"
        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
            throw new IOException("Impossible de stocker le fichier en dehors du répertoire courant");
        }
        //ouvre un inputStream pour lire le contenu du fichier image et copie le contenu dans le fichier de destination
        //si le fichier existe déjà, il est remplacé
        try (InputStream inputStream = imageFile.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        }
        // construire une url ou l'image est accesible
        String imageUrl = "http://localhost:8081/images/" + savedFileName;

        return imageUrl;
    }
    @Override
    public List<Question> getQuestion() {
        return questionRepository.findAll();
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
}
