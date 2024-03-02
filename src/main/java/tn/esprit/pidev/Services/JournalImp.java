package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.StageRepository;
import tn.esprit.pidev.Repositories.TacheRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.Journal;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.Tache;
import tn.esprit.pidev.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class JournalImp implements IServiceJournal {

    private UserRepository userRepository;
    private StageRepository stageRepository;
    private TacheRepository tacheRepository;
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

}
