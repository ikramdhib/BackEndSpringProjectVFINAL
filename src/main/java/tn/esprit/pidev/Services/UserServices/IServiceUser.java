package tn.esprit.pidev.Services.UserServices;

import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.RestControllers.AuthController.Param.AuthenticateResponse;
import tn.esprit.pidev.RestControllers.AuthController.Param.AuthenticationRequest;
import tn.esprit.pidev.RestControllers.AuthController.Param.RegesterRequest;
import tn.esprit.pidev.Services.UserServices.Pagination.PagedResponse;
import tn.esprit.pidev.Services.UserServices.Pagination.SearchRequest;
import tn.esprit.pidev.entities.RoleName;
import tn.esprit.pidev.entities.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IServiceUser {

    User addStudentUser(User user) throws MessagingException, UnsupportedEncodingException;

    String saveImageForUsers(MultipartFile file) throws IOException;

    User addSupervisor(User user) throws MessagingException, UnsupportedEncodingException;

    Boolean blockUser(String id);
    Boolean deBlockUser(String id);

    PagedResponse<User> getAllUserWithRole(RoleName roleName , SearchRequest request);

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

    public User getUserById2(String userId);


    public List<User> getStudentsBySupervisorNote(String encadrantId);
    User deleteUser(String id);

    User addServiceStage(User user) throws MessagingException, UnsupportedEncodingException;

    User updateServiceStage(String id , User user);
}
