package tn.esprit.pidev.Services.ReclamationService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.ReclamationRepository;
import tn.esprit.pidev.entities.Reclamation;

import java.util.List;

@Service
@AllArgsConstructor
public class ReclamationServiceImpl implements IServiceReclamation{

    private final ReclamationRepository reclamationRepository ;
    @Override
    public Reclamation addReclamation(Reclamation reclamation) {
        return reclamationRepository.save(reclamation);
    }

    @Override
    public Reclamation updateReclamation(Reclamation reclamation) {
        return reclamationRepository.save(reclamation);
    }


    @Override
    public List<Reclamation> getAllReclamation() {
        return reclamationRepository.findAll();
    }

    @Override
    public Reclamation getDetailsOfReclamation(String id) {
        return reclamationRepository.findById(id).orElse(null);
    }
}
