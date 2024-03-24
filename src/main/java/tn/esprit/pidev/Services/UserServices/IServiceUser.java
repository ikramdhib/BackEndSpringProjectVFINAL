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
import java.util.List;

public interface IServiceUser {

    User addStudentUser(User user);

    String saveImageForUsers(MultipartFile file) throws IOException;

    User addSupervisor(User user);

    Boolean blockUser(String id);
    Boolean deBlockUser(String id);

    PagedResponse<User> getAllUserWithRole(RoleName roleName , SearchRequest request);

    boolean createPasswordResetToken(String token , UserDetails userDetails);

    String generateToken(User user);

    void changePassword(User user , String newPassword);

    User updateUser(String id , User user);

    User getUserById(String id);

    User updatePassword(String id , String newPass) ;

    User deleteUser(String id);

    User addServiceStage(User user);

    User updateServiceStage(String id , User user);
}
