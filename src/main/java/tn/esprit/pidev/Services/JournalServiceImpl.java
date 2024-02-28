package tn.esprit.pidev.Services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.pidev.Repositories.JournalRepository;
import tn.esprit.pidev.Repositories.StageRepository;
import tn.esprit.pidev.entities.Journal;
import tn.esprit.pidev.entities.Stage;

import java.util.Optional;

@Service
public class JournalServiceImpl implements IServiceJournal{

    private final JournalRepository journalRepository;
    private final StageRepository stageRepository;


    public JournalServiceImpl(JournalRepository journalRepository, StageRepository stageRepository) {

        this.journalRepository = journalRepository;
        this.stageRepository = stageRepository;
    }


    @Override
    public Journal addJournal(Journal journal, String stageId) {
        Stage stage = stageRepository.findById(stageId).orElse(null);

        if (stage != null) {
            // Associer le journal au stage
            journal.setStage(stage);
            // Mettre à jour le champ journal dans le stage
            stage.setJournal(journal);
            // Enregistrer le journal et le stage
            journalRepository.save(journal);
            stageRepository.save(stage);

            return journal;
        } else {
            // Utilisez ResponseStatusException pour générer une réponse HTTP avec un statut spécifique
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Stage not found for ID: " + stageId);
        }
    }
}
