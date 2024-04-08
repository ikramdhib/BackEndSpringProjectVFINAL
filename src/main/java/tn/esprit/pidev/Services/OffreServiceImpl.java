package tn.esprit.pidev.Services;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Repositories.OffreRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.Offre;
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
public class OffreServiceImpl implements IServiceOffre {


    @Autowired
    private OffreRepository offreRepository;
    private UserRepository userRepository;
    private final Path rootLocation = Paths.get("images/");
    // Create
    @Override
    public List<Offre> getAllOffres() {
        return offreRepository.findAll();
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

}





