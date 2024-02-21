package tn.esprit.pidev.Services.UserServices;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.RestControllers.AuthController.Param.AuthenticateResponse;
import tn.esprit.pidev.RestControllers.AuthController.Param.AuthenticationRequest;
import tn.esprit.pidev.RestControllers.AuthController.Param.RegesterRequest;
import tn.esprit.pidev.entities.User;

import java.io.IOException;

public interface IServiceUser {

    User addStudentUser(User user);

    String saveImageForUsers(MultipartFile file) throws IOException;
}
