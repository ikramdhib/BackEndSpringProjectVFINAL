package tn.esprit.pidev.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.DemandeRepo;
import tn.esprit.pidev.Repositories.OffreRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.Demande;
import tn.esprit.pidev.entities.Offre;
import tn.esprit.pidev.entities.User;

import java.util.List;
import java.util.Optional;

@Service
public class DemandeServiceImp implements IServiceDemande {


    @Autowired
    private DemandeRepo demandeRepository;
    @Autowired
    private OffreRepository offreRepo;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }

    @Override
    public Optional<Demande> getDemandeById(String id) {
        return demandeRepository.findById(id);
    }

    @Override
    public Demande adddemande(Demande demande, String userId) {
        User user =userRepository.findById(userId).orElse(null);
        demande.setUser(user);
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

}