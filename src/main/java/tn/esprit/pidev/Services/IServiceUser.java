package tn.esprit.pidev.Services;

import tn.esprit.pidev.RestControllers.AuthController.AuthenticateResponse;
import tn.esprit.pidev.RestControllers.AuthController.AuthenticationRequest;
import tn.esprit.pidev.RestControllers.AuthController.RegesterRequest;
import tn.esprit.pidev.entities.User;

public interface IServiceUser {

   AuthenticateResponse register(RegesterRequest request);
    AuthenticateResponse authenticate(AuthenticationRequest request);

    User addUser(User user);
}
