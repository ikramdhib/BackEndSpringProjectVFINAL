package tn.esprit.pidev.Services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.pidev.Repositories.JournalRepository;
import tn.esprit.pidev.Repositories.StageRepository;
import tn.esprit.pidev.Repositories.TacheRepository;
import tn.esprit.pidev.entities.Journal;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.Tache;
import tn.esprit.pidev.entities.User;

import java.util.*;

@Service
@Slf4j
public class JournalServiceImpl implements IServiceJournal{

    private final JournalRepository journalRepository;
    private final StageRepository stageRepository;
    private TacheRepository tacheRepository;

    public JournalServiceImpl(JournalRepository journalRepository, StageRepository stageRepository) {

        this.journalRepository = journalRepository;
        this.stageRepository = stageRepository;
    }



    public List<Map<String, Object>> getJournalsWithTasksByStudentId(String studentId) {
        List<Stage> stages = stageRepository.findByUser_Id(studentId);
        List<Map<String, Object>> journalDetailsList = new ArrayList<>();
        for (Stage stage : stages) {
            User student = stage.getUser();
            if (student != null) {
                Journal journal = stage.getJournal();
                if (journal != null) {
                    List<Tache> taches = journal.getTaches();
                    if (taches != null && !taches.isEmpty()) {
                        for (Tache tache : taches) {
                            Map<String, Object> journalDetails = new HashMap<>();
                            journalDetails.put("studentFirstName", student.getFirstName());
                            journalDetails.put("studentLastName", student.getLastName());
                            journalDetails.put("journalId", journal.getId());
                            journalDetails.put("tacheDate", tache.getDate());
                            journalDetails.put("tacheLibelle", tache.getLibelle());
                            journalDetailsList.add(journalDetails);
                        }
                    }
                }
            }
        }
        return journalDetailsList;
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

    @Override
    public Journal getJournal(String journalId) {
        return journalRepository.findById(journalId).orElse(null);
    }

}
