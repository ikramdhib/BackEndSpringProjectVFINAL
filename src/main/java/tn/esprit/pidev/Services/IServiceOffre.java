package tn.esprit.pidev.Services;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.entities.Offre;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface IServiceOffre {
    // Create
    Offre  createOffre(Offre offre, String userId);

    // Read
    List<Offre> getAllOffres();

    Optional<Offre> getOffreById(String _id);

    // Update
    Offre updateOffre(String _id, Offre updatedOffre);

    // Delete
    void deleteOffre(String _id);

    Map<String, List<Offre>> groupOffresByEntreprise();

    // Méthode pour télécharger les images et les convertir en URL

    public String saveImage(MultipartFile imageFile)throws IOException;



    public Offre addOffre(Offre o);
    public Offre updateOffre(Offre o);
    public List<Offre> getAllOffre();


    public List<Offre> getOffresWIthUserId(String id);
}

