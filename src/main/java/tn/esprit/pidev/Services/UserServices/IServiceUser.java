package tn.esprit.pidev.Services.UserServices;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.RestControllers.AuthController.Param.AuthenticateResponse;
import tn.esprit.pidev.RestControllers.AuthController.Param.AuthenticationRequest;
import tn.esprit.pidev.RestControllers.AuthController.Param.RegesterRequest;
import tn.esprit.pidev.entities.RoleName;
import tn.esprit.pidev.entities.User;

import java.io.IOException;
import java.util.List;

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

    User getUserById(String id);
}
