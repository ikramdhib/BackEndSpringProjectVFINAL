package tn.esprit.pidev.Services.UserServices;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Configurations.JwtService;
import tn.esprit.pidev.Repositories.StageRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.Services.EmailService;
import tn.esprit.pidev.Services.UserServices.Pagination.PagedResponse;
import tn.esprit.pidev.Services.UserServices.Pagination.SearchRequest;
import tn.esprit.pidev.Services.UserServices.Pagination.Util.SearchRequestUtil;
import tn.esprit.pidev.Services.UserServices.UserListnner.MailingForgetPassListner;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.User;
import jakarta.ws.rs.NotFoundException;

import tn.esprit.pidev.entities.RoleName;

import java.awt.print.Pageable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements IServiceUser {

    public CloudinaryService cloudinaryService;

    private  MailingForgetPassListner mailingForgetPassListner;
    private  UserRepository userRepository;
    private  PasswordEncoder passwordEncoder;
    private StageRepository stageRepository;

    private EmailService emailService;

    @Override
    public User getUserByIdv(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updatePassword(String id, String newPass)  {

        User user = userRepository.findById(id).orElse(null);
        if(user!=null){
            user.setPassword(passwordEncoder.encode(newPass));
        }
        return userRepository.save(user);
    }

    @Override
    public User deleteUser(String id) {
        User user = userRepository.findById(id).orElse(null);
        if(user!=null){
            userRepository.deleteById(id);
        }
     return user;
    }

    @Override
    public User addServiceStage(User user) throws MessagingException, UnsupportedEncodingException {

        StringBuilder codeBuilder = new StringBuilder();

        Random random = new Random();

        // Générer un code de 6 chiffres
        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10); // Générer un chiffre aléatoire entre 0 et 9
            codeBuilder.append(digit); // Ajouter le chiffre à la chaîne de caractères
        }

        user.setRole(RoleName.SERVICE_STAGE);
        user.setActivated(true);
        user.setPassword(passwordEncoder.encode(codeBuilder));
        log.info(user+"999999999999999999999");
        User user1 = userRepository.save(user);
        mailingForgetPassListner.sendWelcomeEmail(user,codeBuilder);
        return user1;
    }

    @Override
    public User updateServiceStage(String id, User user) {
        User user1 = userRepository.findById(id).orElse(null);
        log.info("true8888888888888888888888888888");
        if(user1!=null){
            if(user.pic!=null){
                user1.setPic(user1.pic);
            }
            user1.setAddress(user.address);
            user1.setCin(user.cin);
            user1.setPhoneNumber(user.phoneNumber);
            user1.setLastName(user.lastName);
            user1.setFirstName(user.firstName);
            user1.setLogin(user.login);
        }
        return userRepository.save(user1);
    }


    public String confirmUpdatePass(String id, String newPass , String oldPass) throws MessagingException, UnsupportedEncodingException {

        User user = userRepository.findById(id).orElse(null);

        StringBuilder codeBuilder = new StringBuilder();
        if(user!=null){
            log.info("user1");
            if(passwordEncoder.matches(newPass,user.getPassword())
            || !passwordEncoder.matches(oldPass,user.getPassword())){
                log.info("trueeee1");
                return null;
            }
            //generer le code de verification
            Random random = new Random();

            // Générer un code de 6 chiffres
            for (int i = 0; i < 6; i++) {
                int digit = random.nextInt(10); // Générer un chiffre aléatoire entre 0 et 9
                codeBuilder.append(digit); // Ajouter le chiffre à la chaîne de caractères
            }
            log.info("trueeee2");
            mailingForgetPassListner.sendVerificationEmail(user, codeBuilder);


        }
        return codeBuilder.toString() ;
    }



    private final JwtService jwtService;
    private final Path rootLocation = Paths.get("images");

    public User findByLoginLike(String username) {
        return userRepository.findByLoginLike(username);
    }
    @Override
    public User addStudentUser(User user) throws MessagingException, UnsupportedEncodingException {

        StringBuilder codeBuilder = new StringBuilder();

        Random random = new Random();

        // Générer un code de 6 chiffres
        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10); // Générer un chiffre aléatoire entre 0 et 9
            codeBuilder.append(digit); // Ajouter le chiffre à la chaîne de caractères
        }
        mailingForgetPassListner.sendWelcomeEmail(user,codeBuilder);
      return userRepository.save( User.builder()
                .role(RoleName.ETUDIANT)
                .login(user.login)
                .password(passwordEncoder.encode(codeBuilder))
                .pic(user.pic)
                      .firstName(user.firstName)
                      .lastName(user.lastName)
                      .activated(true)
                      .address(user.address)
                      .cin(user.cin)
                      .level(user.level)
                      .unvId(user.unvId)
                      .phoneNumber(user.phoneNumber)
                      .createdAt(user.createdAt)
                .build());
    }

    @Override
    public String saveImageForUsers(MultipartFile file) throws IOException {
        if(file.isEmpty()){
            throw new IOException("l'image est null");
        }
        String filePath = file.getOriginalFilename();
        log.info(filePath+"--------------------++++++++++++++++++");
        String fileExtention = filePath.substring(filePath.lastIndexOf("."));
        String fileName = UUID.randomUUID()+fileExtention;

        Path fileDestination = this.rootLocation
                .resolve(fileName).normalize().toAbsolutePath();


        if(!fileDestination.getParent().equals(this.rootLocation.toAbsolutePath())){
            throw new IOException("mouvaise destination");
        }

        try(InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream , fileDestination , StandardCopyOption.REPLACE_EXISTING);
        }

        String fileUrl = "/images/"+fileName;
        return fileUrl;
    }

    @Override
    public User addSupervisor(User user) throws MessagingException, UnsupportedEncodingException {
        StringBuilder codeBuilder = new StringBuilder();

        Random random = new Random();

        // Générer un code de 6 chiffres
        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10); // Générer un chiffre aléatoire entre 0 et 9
            codeBuilder.append(digit); // Ajouter le chiffre à la chaîne de caractères
        }
        mailingForgetPassListner.sendWelcomeEmail(user,codeBuilder);
        return userRepository.save(
                User.builder()
                        .lastName(user.lastName)
                        .firstName(user.firstName)
                        .address(user.address)
                        .login(user.login)
                        .password(passwordEncoder.encode(codeBuilder))
                        .phoneNumber(user.phoneNumber)
                        .role(RoleName.ENCADRANT)
                        .activated(false)
                        .emailPro(user.emailPro)
                        .company(user.company)
                        .pic(user.pic)
                        .cin(user.cin)
                        .createdAt(user.createdAt)
                        .build()
        );
    }

    @Override
    public Boolean blockUser(String id) {
        User user = userRepository.findById(id).orElse(null);

        if(user.isActivated()){
            user.setActivated(false);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public Boolean deBlockUser(String id) {
        User user = userRepository.findById(id).orElse(null);

        if(!user.isActivated()){
            user.setActivated(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public PagedResponse<User> getAllUserWithRole(RoleName roleName , final SearchRequest request) {
        final Page<User> response = userRepository.findByRole(roleName,SearchRequestUtil.toPageRequest(request));
        if (response.isEmpty()) {
            return new PagedResponse<>(Collections.emptyList(), 0, response.getTotalElements());
        }

        final List<User> dtos = response.getContent();
        return new PagedResponse<>(dtos, dtos.size(), response.getTotalElements());
    }
    @Override
    public boolean createPasswordResetToken(String token , UserDetails userDetails){
        return jwtService.isTokenValide(token,userDetails);
    }
    @Override
    public String generateToken(User user){
        return jwtService.generatePasswordRessetToken(user);
    }
    @Override
    public void changePassword(User user , String newPassword){
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public User updateUser(String id , User user) {
        User user1 = userRepository.findById(id).orElse(null);

        if(user1.role.equals(RoleName.ENCADRANT)){
            if(user.pic!=null){
                try {
                    user1.setPic(user.pic);
                    cloudinaryService.delete(user.pic);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(user.company!=null){
                user1.setCompany(user.company);
            }
            if(user.login!=null){
                user1.setLogin(user.login);
            }
            user1.setAddress(user.address);
            user1.setPhoneNumber(user.phoneNumber);
            user1.setEmailPro(user.emailPro);
            user1.setCin(user.cin);
        }else{
            if(user.pic!=null){
                try {
                    user1.setPic(user.pic);
                    cloudinaryService.delete(user.pic);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(user.firstName!=null){
                user1.setFirstName(user.firstName);
            }
            if(user.lastName!=null){
                user1.setLastName(user.lastName);
            }
            if(user.level!=null){
                user1.setLevel(user.level);
            }
            if(user.unvId!=null){
                user1.setUnvId(user.unvId);
            }
            if(user.login!=null){
                user1.setLogin(user.login);
            }
            user1.setAddress(user.address);
            user1.setPhoneNumber(user.phoneNumber);
            user1.setCin(user.cin);
        }
        log.info(user1.pic);
        return userRepository.save(user1);
    }


    public List<User> getStudentsBySupervisor(String encadrantId) {
        User encadrant = userRepository.findById(encadrantId)
                .orElseThrow(() -> new IllegalArgumentException("Encadrant with ID " + encadrantId + " not found"));

        List<Stage> stages = stageRepository.findByEncadrant(encadrant);
        List<User> students = stages.stream()
                .map(stage -> {
                    User student = stage.getUser();
                    User studentDTO = new User();
                    studentDTO.setId(student.getId());
                    studentDTO.setRole(RoleName.ETUDIANT);
                    studentDTO.setFirstName(student.getFirstName());
                    studentDTO.setLastName(student.getLastName());
                    studentDTO.setLogin(student.getLogin()); // Ajout du login de l'étudiant
                    studentDTO.setPhoneNumber(student.getPhoneNumber()); // Ajout du numéro de téléphone de l'étudiant
                    studentDTO.setStageId(stage.getId());
                    return studentDTO;
                })
                .collect(Collectors.toList());
        return students;
    }
    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    public User findUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }
    public Date getStageStartDate(String userId, String stageId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getStages() != null) {
                for (Stage stage : user.getStages()) {
                    if (stage.getId().equals(stageId)) {
                        return stage.getStartAt();
                    }
                }
            }
        }
        return null; // Stage non trouvé
    }


    public void rejectStudent(String studentId, String rejectionReason) {
        User student = userRepository.findById(studentId).orElseThrow(() -> new NotFoundException("Étudiant non trouvé avec l'ID : " + studentId));

        // Mettre à jour le statut de l'étudiant
        student.setValidated(true);
        userRepository.save(student);

        // Envoyer un e-mail de rejet à l'étudiant
        if (student.getLogin() != null) {
            String subject = "Rejet de votre demande";
            String text = "Bonjour " + student.getFirstName() + ",\n\n" +
                    "Nous regrettons de vous informer que votre demande a été rejetée pour la raison suivante : " + rejectionReason + ".\n" +
                    "Veuillez prendre les mesures nécessaires pour corriger les problèmes éventuels.\n\n" +
                    "Cordialement,\n" +
                    "Votre équipe.";
            emailService.sendEmail(student.getLogin(), subject, text);

            // Mettre à jour le statut de validation de l'étudiant après l'envoi de l'e-mail
            student.setValidated(true);
            userRepository.save(student);
        } else {
            // Gérer le cas où l'e-mail de l'utilisateur est null
            // Vous pouvez par exemple enregistrer un journal d'erreur ou envoyer une notification à l'administrateur
            // pour signaler que l'e-mail de l'utilisateur est manquant.
        }
    }



    public List<User> getEtudiants() {
        return userRepository.findAll(); // Ou tout autre méthode pour récupérer les étudiants de votre base de données
    }
    public List<Map<String, String>> getStudentsByAllStages(String serviceId) {
        List<Stage> stages = stageRepository.findAll(); // Supposons que vous ayez un repository pour les stages

        // Implémentez la logique pour récupérer les étudiants associés à tous les stages
        // et les retourner sous forme de liste de Map comme expliqué précédemment

        // Exemple simplifié (à adapter à votre logique réelle)
        return stages.stream()
                .filter(stage -> stage.getUser() != null) // Filtrer les stages avec un utilisateur non null
                .map(stage -> {
                    Map<String, String> studentMap = new HashMap<>();
                    User user = stage.getUser();
                    studentMap.put("id", user.getId() != null ? user.getId() : ""); // Ajoutez l'ID de l'étudiant
                    studentMap.put("firstName", user.getFirstName() != null ? user.getFirstName() : "");
                    studentMap.put("lastName", user.getLastName() != null ? user.getLastName() : "");
                    // Ajoutez l'information de validation (supposons que vous ayez un moyen de la récupérer)
                    studentMap.put("validated", user.isValidated() ? "true" : "false");
                    return studentMap;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getEtudiantsAvecStages() {
        return null;
    }
    public List<String> getStudentsNamesByStageService() {
        // Récupérer tous les stages avec des utilisateurs associés
        List<Stage> stagesWithUsers = stageRepository.findStagesWithUsers();

        // Extraire les noms et prénoms des étudiants de ces stages
        return stagesWithUsers.stream()
                .filter(stage -> stage.getUser() != null) // Filtrer les stages avec un utilisateur non null
                .map(stage -> {
                    String firstName = stage.getUser().getFirstName();
                    String lastName = stage.getUser().getLastName();
                    return (firstName != null && lastName != null) ? firstName + " " + lastName : "";
                })
                .filter(name -> !name.isEmpty()) // Filtrer les noms non vides
                .collect(Collectors.toList());
    }


    public User getUserById2(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public List<User> getStudentsBySupervisorNote(String encadrantId) {
        User encadrant = userRepository.findById(encadrantId)
                .orElseThrow(() -> new IllegalArgumentException("Encadrant with ID " + encadrantId + " not found"));

        List<Stage> stages = stageRepository.findByEncadrant(encadrant);
        List<User> students = stages.stream()
                .map(stage -> {
                    User student = stage.getUser();
                    User studentDTO = new User();
                    studentDTO.setId(student.getId());
                    studentDTO.setFirstName(student.getFirstName());
                    studentDTO.setRole(RoleName.ENCADRANT);
                    studentDTO.setLastName(student.getLastName());
                    studentDTO.setLogin(student.getLogin()); // Ajout du login de l'étudiant
                    studentDTO.setPhoneNumber(student.getPhoneNumber()); // Ajout du numéro de téléphone de l'étudiant
                    studentDTO.setStageId(stage.getId());

                    return studentDTO;
                })
                .collect(Collectors.toList());
        students.forEach(student -> {
            // Récupérer les stages associés à l'étudiant
            List<Stage> studentStages = stageRepository.findByUser(student);

            // Assigner les stages à l'étudiant
            student.setStages(studentStages); // Assurez-vous que l'entité User a un setter pour la liste de stages
        });
        return students;
    }

}
