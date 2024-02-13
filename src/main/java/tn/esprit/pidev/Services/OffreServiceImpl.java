package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.OffreRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.Offre;
import tn.esprit.pidev.entities.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OffreServiceImpl implements IServiceOffre {


    @Autowired
    private OffreRepository offreRepository;
    private UserRepository userRepository;

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
      return   offreRepository.save(offre);

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
}



