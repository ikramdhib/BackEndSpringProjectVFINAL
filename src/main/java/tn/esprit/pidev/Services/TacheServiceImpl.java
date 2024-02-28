package tn.esprit.pidev.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.pidev.Repositories.JournalRepository;
import tn.esprit.pidev.Repositories.TacheRepository;
import tn.esprit.pidev.entities.Journal;
import tn.esprit.pidev.entities.Tache;

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
}
