package tn.esprit.pidev.Services.UserServices;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.RestControllers.AuthController.Param.AuthenticateResponse;
import tn.esprit.pidev.entities.User;

@Service
@AllArgsConstructor
public class PasswordResetTokenService {

    public void createPasswordResetTokenUser(User user , String token){

        AuthenticateResponse response = new AuthenticateResponse();
    }
}
