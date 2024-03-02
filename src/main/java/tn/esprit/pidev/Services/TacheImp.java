package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.JournaleRepository;
import tn.esprit.pidev.Repositories.StageRepository;
import tn.esprit.pidev.Repositories.TacheRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.Tache;
import tn.esprit.pidev.entities.User;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.Journal;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@AllArgsConstructor
@Service
@Slf4j
public class TacheImp implements ITacheService{
    private TacheRepository tacheRepository;
    private UserRepository userRepository;
    private JournaleRepository journaleRepository;
    private StageRepository stageRepository;
    private EmailService emailService;


    public List<Tache> getTasksForUserAndStage(String userId, String stageId) {
        User user = userRepository.findById(userId).orElse(null);
        List<Tache> taskList = new ArrayList<>();
        if (user != null && user.getStage() != null) {
            for (Stage stage : user.getStage()) {
                if (stage.getId().equals(stageId) && stage.getJournal() != null && stage.getJournal().getTaches() != null) {
                    for (Tache tache : stage.getJournal().getTaches()) {
                        // Ajoutez des détails de tâche directement à la liste
                        taskList.add(tache);
                    }
                }
            }
        }
        return taskList;
    }

    @Override
    public List<Tache> getTasksForStudent(String studentId) {
        return null;
    }


    ///////Taaaaache
    public List<Tache> getTasksForUser(String userId) {
        User user = userRepository.findById(userId).orElse(null);

        List<Tache> taskList = new ArrayList<>();
        if (user != null && user.getStages() != null) {
            for (Stage stage : user.getStages()) {
                if (stage.getJournal() != null && stage.getJournal().getTaches() != null) {
                    taskList.addAll(stage.getJournal().getTaches());
                }
            }
        }
        return taskList;
    }

  //////ghazpua

  public List<Tache> getTachesByJournalId(String journalId) {
      return tacheRepository.findByJournal_Id(journalId);
  }

    public void validateTask(String tacheId) {
        Tache tache = tacheRepository.findById(tacheId)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));

        // Trouver les stages associés à la tâche
        List<Stage> stages = stageRepository.findByJournal_TachesContains(tache);

        if (stages.isEmpty()) {
            throw new RuntimeException("Aucun stage trouvé pour la tâche spécifiée");
        }

        // Pour chaque stage trouvé, vous pouvez choisir d'exécuter une action spécifique
        for (Stage stage : stages) {
            // Mettre à jour l'état de la tâche pour la valider
            tache.setValidated(true);
            tacheRepository.save(tache);

            // Construire le contenu du courriel
            String subject = "Validation de tâche";
            String message = "Bonjour " + stage.getUser().getFirstName() + ",\n\n" +
                    "Nous sommes heureux de vous informer que votre tâche a été validée avec succès.\n\n" +
                    "Cordialement,\n" +
                    "L'équipe de validation de tâche";

            // Envoyer un e-mail à l'utilisateur pour informer de la validation de la tâche
            emailService.sendEmail(stage.getUser().getLogin(), subject, message);
        }
    }
    public void rejectTask(String tacheId, String rejectionReason) {
        Tache tache = tacheRepository.findById(tacheId).orElseThrow(() -> new RuntimeException("Tâche non trouvée"));
        List<Stage> stages = stageRepository.findByJournal_TachesContains(tache);

        if (stages == null || stages.isEmpty()) {
            throw new RuntimeException("Stage non trouvé pour la tâche spécifiée");
        }

        // Parcourir la liste des stages
        for (Stage stage : stages) {
            // Récupérer l'utilisateur à partir du stage
            User user = stage.getUser();

            // Mettre à jour l'état de la tâche pour la rejeter
            tache.setValidated(false);
            tacheRepository.save(tache);

            // Construire le contenu du courriel
            String subject = "Refus de validation de tâche";
            String message = "Bonjour " + user.getFirstName() + ",\n\n" +
                    "Nous regrettons de vous informer que votre tâche a été refusée pour la raison suivante :\n" +
                    rejectionReason + "\n\n" +
                    "Veuillez prendre les mesures nécessaires pour corriger les problèmes éventuels.\n\n" +
                    "Cordialement,\n" +
                    "L'équipe de validation de tâche";

            // Envoyer un e-mail à l'utilisateur pour informer du rejet de la tâche avec la raison
            emailService.sendEmail(user.getLogin(), subject, message);
        }
    }
}

