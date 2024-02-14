package tn.esprit.pidev.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.DemandeRepo;
import tn.esprit.pidev.Repositories.OffreRepository;
import tn.esprit.pidev.entities.Demande;
import tn.esprit.pidev.entities.Offre;

import java.util.List;
import java.util.Optional;

@Service
public class DemandeServiceImp implements IServiceDemande {


    @Autowired
    private DemandeRepo demandeRepository;
    @Autowired
    private OffreRepository offreRepo;

    @Override
    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }

    @Override
    public Optional<Demande> getDemandeById(String id) {
        return demandeRepository.findById(id);
    }

    @Override
    public Demande createDemande(Demande demande) {
        return demandeRepository.save(demande);
    }

    @Override
    public Demande updateDemande(Demande demande) {
        return demandeRepository.save(demande);
    }

    @Override
    public boolean deleteDemande(String id) {
        if (demandeRepository.existsById(id)) {
            demandeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Demande assignerDemandeAOffre(String demandeId, String offreId) {
        Optional<Demande> demandeOptional = demandeRepository.findById(demandeId);
        Optional<Offre> offreOptional = offreRepo.findById(offreId);

        if (demandeOptional.isPresent() && offreOptional.isPresent()) {
            Demande demande = demandeOptional.get();
            Offre offre = offreOptional.get();

            // Assigner la demande à l'offre
            demande.setOffre(offre);

            // Mettre à jour la demande dans le repository
            demandeRepository.save(demande);

            return demande;
        } else {
            throw new RuntimeException("Demande ou Offre non trouvée.");
        }
    }
}
