package tn.esprit.pidev.Services;

import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tn.esprit.pidev.Repositories.StageRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.User;
import tn.esprit.pidev.entities.RoleName;
import org.slf4j.Logger;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.tika.metadata.Metadata;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StageServiceImpl implements IServiceStage{
    private UserRepository userRepository;
    private StageRepository stageRepository;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(StageServiceImpl.class);




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
            // Récupérer l'utilisateur associé au stage
            User user = stage.getUser();

            // Créer un nouvel encadrant à partir des informations de l'utilisateur
            User encadrant = new User();
            encadrant.setFirstName(user.getFirstName());
            encadrant.setLastName(user.getLastName());
            encadrant.setPhoneNumber(user.getPhoneNumber());
            encadrant.setEmailPro(user.getEmailPro());
            encadrant.setPassword(passwordEncoder.encode(generateRandomPassword()));
            encadrant.setCreatedAt(new Date());
            encadrant.setRole(RoleName.ENCADRANT); // Définir le rôle d'encadrant

            // Sauvegarder le nouvel encadrant dans la table User
            userRepository.save(encadrant);

            // Associer l'encadrant au stage en tant qu'encadrant
            stage.setEncadrant(encadrant);

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

    @Autowired
    public StageServiceImpl(StageRepository stageRepository, UserRepository userRepository) {
        this.stageRepository = stageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void ajouterEtAffecterStageAUtilisateur(Stage stage, String userId) {

        User user = userRepository.findById(userId).orElse(null);
        stage.setUser(user);
        stageRepository.save(stage);
        log.info(stage.type.name()+"&&&&&&&&&&&&&&&&&&&");

    }

    @Override
    public void saveDemandeStage(String userId, String demandeStageContent) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + userId));

        Stage stage = new Stage();
        stage.setUser(user);
        stage.setDemandeS(demandeStageContent);

        stageRepository.save(stage);
    }

    @Override
    public List<Stage> getStagesByUserId(String userId) {
        return stageRepository.findByUserId(userId);
    }

    public List<Stage> getStagesByEncadrantId(String encadrantId) {
        return stageRepository.findByEncadrantId(encadrantId);
    }


    public InputStream getRapportDeStagePdf(String userId) {
        List<Stage> stages = stageRepository.findByUserId(userId);
        if (!stages.isEmpty() && stages.get(0).getRapportPdf() != null) {
            return new ByteArrayInputStream(stages.get(0).getRapportPdf());
        } else {
            return null;
        }
    }
    public InputStream getAttestationPdfForUser(String userId) {
        List<Stage> stages = stageRepository.findByUserId(userId);
        if (!stages.isEmpty() && stages.get(0).getAttestationPdf() != null) {
            return new ByteArrayInputStream(stages.get(0).getAttestationPdf());
        } else {
            return null;
        }
    }
    @Transactional
    public void uploadPDF(String stageId, MultipartFile pdfFile) throws IOException {
        Stage stage = stageRepository.findById(stageId).orElseThrow(() -> new IllegalArgumentException("Stage non trouvé"));
        stage.setAttestationPdf(pdfFile.getBytes());
        stageRepository.save(stage);
    }
    ////////IAAA
    public InputStream getRapportDeStagePdfAvecOCR(String userId) {
        List<Stage> stages = stageRepository.findByUserId(userId);
        if (!stages.isEmpty() && stages.get(0).getRapportPdf() != null) {
            byte[] pdfBytes = stages.get(0).getRapportPdf();

            // Utiliser Tesseract OCR pour extraire le texte du PDF
            String texteExtrait = extraireTexteAvecTIKA(pdfBytes);
            if (texteExtrait != null && !texteExtrait.isEmpty()) {
                // Si le texte a été extrait avec succès, retourner un InputStream du texte
                return new ByteArrayInputStream(texteExtrait.getBytes());
            } else {
                // En cas d'erreur lors de l'extraction du texte, retourner null
                return null;
            }
        } else {
            return null;
        }
    }



    private String extraireTexteAvecTIKA(byte[] pdfBytes) {
        try {
            logger.info("Début de l'extraction du texte du PDF");

            // Créer un parseur Tika
            AutoDetectParser parser = new AutoDetectParser();

            BodyContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata(); // Utilisation de org.apache.tika.metadata.Metadata
            ParseContext parseContext = new ParseContext();

            // Extraire le texte du PDF
            try (InputStream stream = new ByteArrayInputStream(pdfBytes)) {
                parser.parse(stream, handler, metadata, parseContext);
                String texteExtrait = handler.toString();

                // Vérifier si le texte extrait respecte les normes
                if (verifierNormes(texteExtrait)) {
                    logger.info("Extraction du texte terminée avec succès");
                    return texteExtrait;
                } else {
                    logger.warn("Le texte extrait ne respecte pas les normes");
                    return null; // Renvoie null si le texte n'est pas conforme aux normes
                }
            } catch (SAXException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException | TikaException e) {
            logger.error("Erreur lors de l'extraction du texte du PDF : {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private boolean verifierNormes(String texteExtrait) {
        // Vérifier le titre de taille 16
        boolean titreConforme = texteExtrait.contains("Titre du rapport") && texteExtrait.indexOf("Titre du rapport") < 40;

        // Vérifier la taille du paragraphe (nombre de mots par paragraphe)
        String[] paragraphes = texteExtrait.split("\n\n"); // Supposer que les paragraphes sont séparés par deux sauts de ligne
        int nombreParagrapheTailleCorrecte = 0;
        for (String paragraphe : paragraphes) {
            if (paragraphe.split("\\s+").length >= 50) { // Supposer que 50 mots représentent une taille de paragraphe de 10
                nombreParagrapheTailleCorrecte++;
            }
        }
        boolean paragrapheConforme = nombreParagrapheTailleCorrecte >= 20; // Au moins 20 paragraphes de taille 10

        // Vérifier le nombre de pages (nombre de retours à la ligne)
        int nombreRetoursLigne = texteExtrait.split("\n").length;
        boolean nombrePagesConforme = nombreRetoursLigne / 50 >= 20; // Supposer que 50 retours à la ligne représentent une page

        return titreConforme && paragrapheConforme && nombrePagesConforme;
    }
    @Override
    public Stage updateStage(String stageId, Stage updatedStage) {

            Stage existingStage = stageRepository.findById(stageId).orElse(null);

            if (existingStage != null) {
                // Mettez à jour les champs nécessaires du stage existant avec les nouvelles valeurs
                existingStage.setNomSociete(updatedStage.getNomSociete());
                existingStage.setNumSociete(updatedStage.getNumSociete());
                existingStage.setEmailSociete(updatedStage.getEmailSociete());
                existingStage.setNomCoach(updatedStage.getNomCoach());
                existingStage.setPrenomCoach(updatedStage.getPrenomCoach());
                existingStage.setNumCoach(updatedStage.getNumCoach());
                existingStage.setEmailCoach(updatedStage.getEmailCoach());
                existingStage.setStartAt(updatedStage.getStartAt());
                existingStage.setEndAt(updatedStage.getEndAt());
                existingStage.setType(updatedStage.getType());
                // Enregistrez les modifications dans la base de données
                return stageRepository.save(existingStage);
            }

            return null; // Ou lancez une exception selon vos besoins
        }

    @Override
    public void deleteStageById(String stageId) {
        stageRepository.deleteById(stageId);
    }

    @Override
    public Stage getStageById(String stageId) {
        return stageRepository.findById(stageId).orElse(null);
    }

    @Override
    public boolean isJournalAssociated(String stageId) {
        Stage stage = stageRepository.findById(stageId).orElse(null);
        boolean isAssociated = (stage != null && stage.getJournal() != null);

        // Ajouter des logs de débogage
        System.out.println("Stage ID: " + stageId);
        System.out.println("Is Journal Associated: " + isAssociated);

        return isAssociated;
    }

    @Override
    public Stage updateStage2(String stageId, Stage updatedStage) {
        Stage existingStage = stageRepository.findById(stageId).orElse(null);
        if (existingStage != null) {
            // Mettre à jour les champs du stage avec les valeurs du stage mis à jour
            existingStage.setRapportPdf(updatedStage.getRapportPdf());
            // Vous pouvez mettre à jour d'autres champs ici si nécessaire
            // Enregistrer le stage mis à jour dans la base de données
            return stageRepository.save(existingStage);
        }
        return null; // ou lancez une exception appropriée si le stage n'est pas trouvé
    }




    @Override
    public boolean rapportExistePourStage(String stageId) {
        Stage stage = stageRepository.findById(stageId).orElse(null);
        return stage != null && stage.getRapportPdf() != null;
    }

    @Override
    public Stage findById(String id) {
        return stageRepository.findById(id).orElse(null);
    }


    @Override
    public List<Stage> getAllStageWithUsrId(String id){
        return stageRepository.findByUser_Id(id);
    }
}





