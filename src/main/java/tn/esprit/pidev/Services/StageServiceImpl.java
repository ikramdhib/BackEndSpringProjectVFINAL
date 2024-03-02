package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Repositories.RoleRepository;
import tn.esprit.pidev.Repositories.StageRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.Role;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.User;
import tn.esprit.pidev.entities.RoleName;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
    /////////////attestation
    public boolean addAttestationToStage(String stageId, String encadrantId, String etudiantId, MultipartFile pdfFile) {
        Optional<Stage> optionalStage = stageRepository.findById(stageId);
        Optional<User> optionalEncadrant = userRepository.findById(encadrantId);
        Optional<User> optionalEtudiant = userRepository.findById(etudiantId);

        if (optionalStage.isEmpty() || optionalEncadrant.isEmpty() || optionalEtudiant.isEmpty()) {
            return false; // Stage, encadrant ou étudiant non trouvé
        }

        Stage stage = optionalStage.get();
        User encadrant = optionalEncadrant.get();
        User etudiant = optionalEtudiant.get();

        try {
            byte[] pdfData = pdfFile.getBytes();
            stage.setAttestationPdf(pdfData); // Enregistrez les données PDF dans la base de données
            stage.setEncadrant(encadrant);
            stage.setUser(etudiant);
            stageRepository.save(stage);
            return true; // Succès de l'ajout de l'attestation au stage
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Erreur lors de la lecture du fichier PDF
        }
    }


    @Override
    public Stage saveStage(Stage stage) {
        return stageRepository.save(stage);
    }
    public Stage findStageById(String stageId) {
        // Implémentation de la méthode pour trouver le stage par son identifiant
        return stageRepository.findById(stageId).orElse(null);
    }

    public Stage findStageByUserAndEncadrant(User etudiant, User encadrant) {
        return stageRepository.findByUserAndEncadrant(etudiant, encadrant);
    }
    /////////////TimeLineDateeeee
    public List<String[]> getStudentTimeline(String studentId) {
        List<Stage> stages = stageRepository.findByUserId(studentId);
        List<String[]> timeline = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Stage stage : stages) {
            // Convertir les objets Date en chaînes de caractères représentant des dates
            String startDateStr = dateFormat.format(stage.getStartAt());
            String endDateStr = dateFormat.format(stage.getEndAt());

            // Parse la date de début
            LocalDate startDate = LocalDate.parse(startDateStr);
            // Parse la date de fin
            LocalDate endDate = LocalDate.parse(endDateStr);

            // Ajoute une semaine à la date de fin pour obtenir la date de dépôt du rapport et du journal
            LocalDate reportDate = endDate.plusWeeks(1);

            // Ajoute les dates formatées à la liste de la timeline
            String[] timelineEntry = {startDate.toString(), endDate.toString(), reportDate.toString()};
            timeline.add(timelineEntry);
        }
        return timeline;
    }
    ////////
    public List<String[]> getStudentTimeline(String studentId, String stageId) {
        Optional<User> optionalUser = userRepository.findById(studentId);
        Optional<Stage> optionalStage = stageRepository.findById(stageId);

        if (optionalUser.isEmpty() || optionalStage.isEmpty()) {
            return null; // Gérer le cas où l'utilisateur ou le stage n'existe pas
        }

        User student = optionalUser.get();
        Stage stage = optionalStage.get();

        if (!stage.getUser().getId().equals(studentId)) {
            return null; // Gérer le cas où le stage ne correspond pas à l'utilisateur
        }

        List<String[]> timeline = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Convertir les dates en chaînes de caractères
        LocalDate startDate = stage.getStartAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = stage.getEndAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate journalDate = endDate.plusWeeks(2);
        LocalDate attestationDate = endDate.plusWeeks(1);
        LocalDate reportDate = endDate.plusWeeks(2);
        // Ajouter les dates formatées à la liste de la timeline
        String[] timelineEntry = {startDate.format(dateFormatter), endDate.format(dateFormatter), journalDate.format(dateFormatter), attestationDate.format(dateFormatter), reportDate.format(dateFormatter)};
        timeline.add(timelineEntry);

        return timeline;
    }
    @Override
    public List<Stage> getStagesByUserId(String userId) {
        return stageRepository.findByUserId(userId);
    }

    public List<Stage> getStagesByEncadrantId(String encadrantId) {
        return stageRepository.findByEncadrantId(encadrantId);
    }

}





