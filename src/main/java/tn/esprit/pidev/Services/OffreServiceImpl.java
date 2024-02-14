package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.OffreRepository;
import tn.esprit.pidev.entities.Offre;

import java.util.List;

@Service
@AllArgsConstructor
public class OffreServiceImpl implements IServiceOffre{

    private OffreRepository offreRepository;
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
    public Offre getOffreById(String id) {
        return offreRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteOffre(String id) {

    }
}
