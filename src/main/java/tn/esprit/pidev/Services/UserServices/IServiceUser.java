package tn.esprit.pidev.Services.UserServices;

import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.RestControllers.AuthController.Param.AuthenticateResponse;
import tn.esprit.pidev.RestControllers.AuthController.Param.AuthenticationRequest;
import tn.esprit.pidev.RestControllers.AuthController.Param.RegesterRequest;
import tn.esprit.pidev.entities.RoleName;
import tn.esprit.pidev.entities.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IServiceUser {

    User addStudentUser(User user);

    String saveImageForUsers(MultipartFile file) throws IOException;

    User addSupervisor(User user);

    Boolean blockUser(String id);

    List<User> getAllUserWithRole(RoleName roleName);

    boolean createPasswordResetToken(String token , UserDetails userDetails);

    String generateToken(User user);

    void changePassword(User user , String newPassword);

    User updateUser(String id , User user);

    User getUserByIdv(String id);


    User updatePassword(String id , String newPass) ;


    public List<User> getStudentsBySupervisor(String encadrantId);
    public User findUserById(String userId) ;
    public Optional<User> getUserById(String userId) ;
    public Date getStageStartDate(String userId, String stageId) ;

    public List<Map<String, String>> getStudentsByAllStages(String serviceId) ;
    public List<User> getEtudiantsAvecStages() ;

    public List<User> getEtudiants() ;
    public List<String> getStudentsNamesByStageService() ;
}
