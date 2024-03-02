package tn.esprit.pidev.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.pidev.Repositories.JournalRepository;
import tn.esprit.pidev.Repositories.TacheRepository;
import tn.esprit.pidev.entities.Journal;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.Tache;

import java.util.List;
import java.util.Optional;

@Service
public class TacheServiceImpl implements IServiceTache {

    private final TacheRepository tacheRepository;
    private final JournalRepository journalRepository;
    @Autowired
    public TacheServiceImpl(TacheRepository tacheRepository, JournalRepository journalRepository) {
        this.tacheRepository = tacheRepository;
        this.journalRepository = journalRepository;
    }


    @Override
    public Tache addTacheWithJournal(Tache tache, String journalId) {
        Optional<Journal> optionalJournal = journalRepository.findById(journalId);

        if (optionalJournal.isPresent()) {
            Journal existingJournal = optionalJournal.get();
            tache.setJournal(existingJournal);

            return tacheRepository.save(tache);
        } else {
            // Utilisez ResponseStatusException pour générer une réponse HTTP avec un statut spécifique
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Le journal avec l'ID " + journalId + " n'a pas été trouvé");
        }
    }

    @Override
    public List<Tache> getTachesByStageId(String stageId) {
        return tacheRepository.findByJournal_Stage_Id(stageId);
    }

    @Override
    public List<Tache> getTachesByJournalId(String journalId) {
        return tacheRepository.findByJournal_Id(journalId);
    }

    @Override
    public Tache updateTache(String tacheId, Tache updatedTache) {
        Tache existingTache = tacheRepository.findById(tacheId).orElse(null);

        if (existingTache != null) {
            // Mettez à jour les champs nécessaires du stage existant avec les nouvelles valeurs
            existingTache.setDate(updatedTache.getDate());
            existingTache.setLibelle(updatedTache.getLibelle());
            // Enregistrez les modifications dans la base de données
            return tacheRepository.save(existingTache);
        }
        return null; // Ou lancez une exception selon vos besoins
    }
}
