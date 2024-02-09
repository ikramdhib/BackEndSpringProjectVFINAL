package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.User;

import tn.esprit.pidev.Configurations.JwtService;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.RestControllers.AuthController.AuthenticateResponse;
import tn.esprit.pidev.RestControllers.AuthController.AuthenticationRequest;
import tn.esprit.pidev.RestControllers.AuthController.RegesterRequest;
import tn.esprit.pidev.entities.RoleName;
import tn.esprit.pidev.entities.User;
@Service
@AllArgsConstructor
public class UserServiceImpl implements IServiceUser  {



    private final UserRepository userRepository;
    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    @Override
    public AuthenticateResponse register(RegesterRequest request) {
       var  user = User.builder()
               .firstName(request.getFirstName())
               .lastName(request.getLastName())
               .login(request.getLogin())
               .password(passwordEncoder.encode(request.getPassword()))
               .role(RoleName.ENCADRANT)
               .build();
       userRepository.save(user);

       var jwtToken = jwtService.generateToken(user);
        return AuthenticateResponse.builder().
                token(jwtToken).
                build();
    }

    @Override
    public AuthenticateResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );
       var user = userRepository.findByLogin(request.getLogin())
               .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticateResponse.builder().
                token(jwtToken).
                build();
    }
}
