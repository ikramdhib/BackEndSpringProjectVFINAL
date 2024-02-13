package tn.esprit.pidev.Services.UserServices;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.User;

import tn.esprit.pidev.Configurations.JwtService;
import tn.esprit.pidev.RestControllers.AuthController.Param.AuthenticateResponse;
import tn.esprit.pidev.RestControllers.AuthController.Param.AuthenticationRequest;
import tn.esprit.pidev.RestControllers.AuthController.Param.RegesterRequest;
import tn.esprit.pidev.entities.RoleName;

import java.io.IOException;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IServiceUser {



    private final UserRepository userRepository;
    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }
    public User findByLoginLike(String username) {
        return userRepository.findByLoginLike(username);
    }


}
