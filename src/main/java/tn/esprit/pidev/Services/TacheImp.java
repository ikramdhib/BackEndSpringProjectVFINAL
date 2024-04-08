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
import java.util.Optional;
import java.util.stream.Collectors;

import static tn.esprit.pidev.entities.RoleName.ETUDIANT;

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
        if (user != null && user.getStages() != null) {
            for (Stage stage : user.getStages()) {
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
        Optional<Tache> optionalTache = tacheRepository.findById(tacheId);
        if (optionalTache.isPresent()) {
            Tache tache = optionalTache.get();
            Journal journalTache = tache.getJournal();
            List<Stage> stages = stageRepository.findByJournal(journalTache);

            if (stages.isEmpty()) {
                throw new RuntimeException("Aucun stage trouvé pour le journal associé à la tâche spécifiée");
            }

            for (Stage stage : stages) {
                Journal journalStage = stage.getJournal();
                if (!journalTache.equals(journalStage)) {
                    continue;
                }

                // Vérifier le rôle de l'utilisateur associé au stage
                User user = stage.getUser();
                if (user != null && user.getEmailPro() != null)  {
                    String studentEmail = user.getEmailPro();
                    tache.setValidated(true);
                    tacheRepository.save(tache);

                    String subject = "Validation de tâche";
                    // Ajout du libellé et de la date de la tâche dans le message
                    String message = "Bonjour " + user.getFirstName() + ",\n\n" +
                            "Nous sommes heureux de vous informer que votre tâche intitulée '" + tache.getLibelle() + "' créée le " + tache.getDate() + " a été validée avec succès.\n\n" +
                            "Cordialement,\n" +
                            "L'équipe de validation de tâche";

                    try {
                        emailService.sendEmail(studentEmail, subject, message);
                    } catch (Exception e) {
                        // Gérer les exceptions liées à l'envoi de l'e-mail
                        throw new RuntimeException("Erreur lors de l'envoi de l'e-mail de validation de tâche", e);
                    }
                }
            }
        } else {
            throw new RuntimeException("Tâche non trouvée");
        }
    }

    public void rejectTask(String tacheId, String rejectionReason) {
        Optional<Tache> optionalTache = tacheRepository.findById(tacheId);
        if (optionalTache.isPresent()) {
            Tache tache = optionalTache.get();
            Journal journalTache = tache.getJournal();
            List<Stage> stages = stageRepository.findByJournal(journalTache);

            if (stages.isEmpty()) {
                throw new RuntimeException("Aucun stage trouvé pour le journal associé à la tâche spécifiée");
            }

            for (Stage stage : stages) {
                Journal journalStage = stage.getJournal();
                if (!journalTache.equals(journalStage)) {
                    continue;
                }

                // Vérifier le rôle de l'utilisateur associé au stage
                User user = stage.getUser();
                if (user != null && user.getEmailPro() != null)  {
                    String userMail = user.getEmailPro();
                    tache.setValidated(false); // Rejeter la tâche
                    tacheRepository.save(tache);

                    String subject = "Refus de validation de tâche";
                    // Ajouter la raison du refus dans le message
                    String message = "Bonjour " + user.getFirstName() + ",\n\n" +
                            "Nous regrettons de vous informer que votre tâche intitulée '" + tache.getLibelle() + "' a été refusée pour la raison suivante :\n" +
                            rejectionReason + "\n\n" +
                            "Veuillez prendre les mesures nécessaires pour corriger les problèmes éventuels.\n\n" +
                            "Cordialement,\n" +
                            "L'équipe de validation de tâche";

                    try {
                        emailService.sendEmail(userMail, subject, message);
                    } catch (Exception e) {
                        // Gérer les exceptions liées à l'envoi de l'e-mail
                        throw new RuntimeException("Erreur lors de l'envoi de l'e-mail de refus de tâche", e);
                    }
                }
            }
        } else {
            throw new RuntimeException("Tâche non trouvée");
        }
    }

}

