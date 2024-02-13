package tn.esprit.pidev.Services;

import org.bson.types.ObjectId;
import tn.esprit.pidev.entities.Offre;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

}

