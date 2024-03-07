package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.HistoriqueRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.Historique;
import tn.esprit.pidev.entities.User;

import java.util.List;

@Service
@AllArgsConstructor
public class HistoriqueServiceImpl implements IServiceHistorique {
   private HistoriqueRepository historiqueRepository;
   private UserRepository userRepository;
    @Override
    public void saveHistorique(Historique historique) {
        User user = userRepository.findById("65d5faf88ecbf72fd4d359f2").orElse(null);
        historique.setUser(user);
       historiqueRepository.save(historique);
    }
    @Override
    public List<Historique> findBuUserId(String userId) {
        return historiqueRepository.findByUserId(userId);
    }

    @Override
    public void delete(String historiqueId) {
        historiqueRepository.deleteById(historiqueId);
    }
}
