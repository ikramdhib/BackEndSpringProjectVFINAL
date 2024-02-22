package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.RoleRepository;
import tn.esprit.pidev.Repositories.StageRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.Role;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.User;
import tn.esprit.pidev.entities.RoleName;

import java.util.*;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;

@Service
@AllArgsConstructor
public class StageServiceImpl implements IServiceStage{
    private UserRepository userRepository;
    private StageRepository stageRepository;
    private RoleRepository roleRepository;
    private EmailService emailService;




    public Map<String, String> getAllStagesWithUserNames() {
        List<Stage> stages = stageRepository.findAll();

        Map<String, String> stageUserNames = new HashMap<>();
        for (Stage stage : stages) {
            // Exclure les stages dont l'attribut 'etat' est true
            if (stage.isEtat()) {
                continue; // Passer au prochain stage
            }

            String userName = (stage.getUser() != null) ? stage.getUser().getFirstName() + " " + stage.getUser().getLastName() : "Unknown";
            String stageInfo = "Stage(id=" + stage.getId() +
                    ", startAt=" + stage.getStartAt() +
                    ", endAt=" + stage.getEndAt() +
                    ", type=" + stage.getType() +
                    ", nomCoach=" + stage.getNomCoach() +
                    ", prenomCoach=" + stage.getPrenomCoach() +
                    ", numCoach=" + stage.getNumCoach() +
                    ", emailCoach=" + stage.getEmailCoach() +
                    ", user=" + userName +
                    ")";
            stageUserNames.put(stageInfo, userName);
        }

        return stageUserNames;
    }
    public void sendEmailToEncadrant(String stageId) {
        Stage stage = stageRepository.findById(stageId).orElse(null);
        if (stage != null && stage.getUser() != null) {
            User user = stage.getUser();
            String encadrantEmail = stage.getEmailCoach();
            String subject = "Validation de stage";
            String text = "Bonjour, voici vos identifiants pour valider le stage de l'étudiant : \n"
                    + "E-mail : " + user.getEmailPro() + "\n"
                    + "Mot de passe : " + generateRandomPassword();
            emailService.sendEmail(encadrantEmail, subject, text);

            // Mettre à jour l'attribut 'etat' du stage à true
            stage.setEtat(true);

            // Sauvegarder les modifications du stage
            stageRepository.save(stage);
        }
    }



    private String generateRandomPassword() {
        String password = UUID.randomUUID().toString().replace("-", "").substring(0, 8); // Obtenez une chaîne de 8 caractères
        return password;
    }

    public void sendEmailToStudent(String stageId, String reason) {
        Stage stage = stageRepository.findById(stageId).orElse(null);
        if (stage != null && stage.getUser() != null) {
            User user = stage.getUser();
            String studentEmail = user.getEmailPro(); // Suppose que l'e-mail professionnel de l'étudiant est utilisé

            // Vérifier si l'e-mail de l'étudiant est non null avant de l'utiliser
            if (studentEmail != null) {
                String subject = "Refus de validation de stage";
                String text = "Bonjour " + user.getFirstName() + ",\n\n" +
                        "Nous regrettons de vous informer que les données de votre stage n'ont pas été validées pour la raison suivante : " + reason + ".\n" +
                        "Veuillez prendre les mesures nécessaires pour corriger les problèmes éventuels.\n\n" +
                        "Cordialement,\n" +
                        "L'équipe de validation de stage";
                emailService.sendEmail(studentEmail, subject, text);
                stage.setEtat(true);
            } else {
                // Gérer le cas où l'e-mail de l'utilisateur est null
                // Vous pouvez par exemple enregistrer un journal d'erreur ou envoyer une notification à l'administrateur
                // pour signaler que l'e-mail de l'utilisateur est manquant.
            }
        }
    }


    @Override
    public List<User> getStudentsByEncadrantId(String encadrantId) {
        return stageRepository.findByUser_Id(encadrantId)
                .stream()
                .map(Stage::getUser)
                .collect(Collectors.toList());
    }
    public void updateEncadrantInfoAndRemoveFromStage(String stageId) {
        Stage stage = stageRepository.findById(stageId).orElse(null);
        if (stage != null && stage.getUser() != null) {
            // Récupérer les informations de l'encadrant depuis le stage
            String firstName = stage.getNomCoach();
            String lastName = stage.getPrenomCoach();
            String phoneNumber = stage.getNumCoach();
            String emailPro = stage.getEmailCoach();

            // Créer un nouvel utilisateur avec le rôle "encadrant"
            User encadrant = new User();
            encadrant.setFirstName(firstName);
            encadrant.setLastName(lastName);
            encadrant.setPhoneNumber(phoneNumber);
            encadrant.setEmailPro(emailPro);
            encadrant.setRole(RoleName.ENCADRANT); // Définir le rôle d'encadrant

            // Sauvegarder le nouvel utilisateur dans la table User
            userRepository.save(encadrant);

            // Ajouter l'encadrant au stage sans supprimer l'affectation de l'étudiant
            stage.addEncadrant(encadrant); // Méthode à implémenter dans la classe Stage


            // Sauvegarder les modifications du stage avec l'encadrant ajouté
            stageRepository.save(stage);
        }
    }

}





