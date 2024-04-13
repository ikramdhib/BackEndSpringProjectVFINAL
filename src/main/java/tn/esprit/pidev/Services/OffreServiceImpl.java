package tn.esprit.pidev.Services;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Repositories.OffreRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.Offre;
import tn.esprit.pidev.entities.Type;
import tn.esprit.pidev.entities.User;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class OffreServiceImpl implements IServiceOffre {


    private OffreRepository offreRepository;
    private UserRepository userRepository;
    private final Path rootLocation = Paths.get("images/");
    // Create
    @Override
    public List<Offre> getAllOffres() {
        return  offreRepository.findAll();
    }


    @Override
    public Offre addOffre(Offre o) {
        return offreRepository.save(o);
    }

    @Override
    public Offre updateOffre(Offre o) {
        return offreRepository.save(o);
    }

    @Override
    public List<Offre> getAllOffre() {
        return offreRepository.findAll();
    }

    @Override
    public List<Offre> getOffresWIthUserId(String id) {
        return offreRepository.findOffreByUserIdLike(id);
    }

    @Override
    public Map<String, List<Offre>> groupOffresByEntreprise() {
        return getAllOffres().stream()
                .collect(Collectors.groupingBy(Offre::getNomEntreprise));
    }

    @Override
    public Optional<Offre> getOffreById(String _id) {
        return offreRepository.findById(_id);
    }

    @Override
    public Offre createOffre(Offre offre, String userId) {

        User user = userRepository.findById(userId).orElse(null);

        log.info(user+"-----------------------------------------------");
        offre.setUser(user);
        return offreRepository.save(offre);

    }


    @Override
    public Offre updateOffre(String _id, Offre updatedOffre) {
        Optional<Offre> existingOffreOptional = offreRepository.findById(_id);
        if (existingOffreOptional.isPresent()) {
            Offre existingOffre = existingOffreOptional.get();
            existingOffre.setNomEntreprise(updatedOffre.getNomEntreprise());
            existingOffre.setNomEncadrant(updatedOffre.getNomEncadrant());
            existingOffre.setPrenomEncadrant(updatedOffre.getPrenomEncadrant());
            existingOffre.setEmail(updatedOffre.getEmail());
            existingOffre.setDescription(updatedOffre.getDescription());
            existingOffre.setDatedebut_stage(updatedOffre.getDatedebut_stage());
            existingOffre.setDatefin_stage(updatedOffre.getDatefin_stage());
            return offreRepository.save(existingOffre);
        } else {
            return null;
        }
    }

    @Override
    public void deleteOffre(String _id) {
        offreRepository.deleteById(_id);
    }



    @Override
    public String saveImage(MultipartFile imageFile) throws IOException {
        if (imageFile.isEmpty()) {
            throw new IOException("Le fichier reçu est vide");
        }

        String originalFilename = imageFile.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String savedFileName = UUID.randomUUID().toString() + extension;
        Path destinationFile = this.rootLocation.resolve(Paths.get(savedFileName))
                .normalize().toAbsolutePath();

        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
            throw new IOException("Impossible de stocker le fichier en dehors du répertoire courant");
        }

        try (InputStream inputStream = imageFile.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        }

        String imageUrl = "http://localhost:8081/images/" + savedFileName;

        return imageUrl;
    }
    @PostConstruct // Pour créer le répertoire des images au démarrage si nécessaire
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le dossier pour les images", e);
        }
    }




    public Offre likeOffre(String offreId, String userId) {
        Optional<Offre> offreOpt = offreRepository.findById(offreId);
        Optional<User> userOpt = userRepository.findById(userId);
        if (offreOpt.isPresent() && userOpt.isPresent()) {
            Offre offre = offreOpt.get();
            User user = userOpt.get();
            offre.like(user);
            return offreRepository.save(offre);
        }
        // Gérer le cas où l'offre ou l'utilisateur n'existe pas...
        throw new RuntimeException("Offre ou Utilisateur non trouvé");
    }

    public Offre dislikeOffre(String offreId, String userId) {
        Optional<Offre> offreOpt = offreRepository.findById(offreId);
        Optional<User> userOpt = userRepository.findById(userId);
        if (offreOpt.isPresent() && userOpt.isPresent()) {
            Offre offre = offreOpt.get();
            User user = userOpt.get();
            offre.dislike(user);
            return offreRepository.save(offre);
        }
        // Gérer le cas où l'offre ou l'utilisateur n'existe pas...
        throw new RuntimeException("Offre ou Utilisateur non trouvé");
    }


    public Offre getOffreByIdv2(String id) {
        return offreRepository.findById(id).orElse(null);
    }

    public List<Offre> getAllOffresForUser(String userId) {
        return offreRepository.findByUser_Id(userId); // Assuming you have a method in your repository to find offers by user ID
    }

    @Override
    public List<Offre> findByType(Type type) {
        return offreRepository.findByType(type);
    }

}





