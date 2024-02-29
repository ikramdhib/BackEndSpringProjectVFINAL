package tn.esprit.pidev.Services.UserServices;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Configurations.JwtService;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.RestControllers.AuthController.Param.AuthenticateResponse;
import tn.esprit.pidev.RestControllers.AuthController.Param.AuthenticationRequest;
import tn.esprit.pidev.RestControllers.AuthController.Param.RegesterRequest;
import tn.esprit.pidev.entities.RoleName;
import tn.esprit.pidev.entities.User;

import java.io.IOException;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationService{

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;
    public AuthenticateResponse register(RegesterRequest request) {
        var  user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .login(request.getLogin())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRoleName())
                .build();
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken=jwtService.generateRefreshToken(user);
        return AuthenticateResponse.builder()
                .acesstoken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticateResponse authenticate(AuthenticationRequest request) {
log.info("hello");
log.info(request.getLogin());
log.info(request.getPassword());
log.info(String.valueOf(userRepository.findByLogin(request.getLogin())));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByLogin(request.getLogin())
                .orElseThrow();
        log.info(String.valueOf(user == null)+"----------------------------------------------------------");
        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + user.login);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken=jwtService.generateRefreshToken(user);
        return AuthenticateResponse.builder().
                acesstoken(jwtToken).
                refreshToken(refreshToken)
                .build();

    }
    public void refreshToken(HttpServletRequest request ,
                             HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken ;
        final String userLogin ;
        if(authHeader == null ||!authHeader.startsWith("Bearer ")){
            return ;
        }
        refreshToken = authHeader.substring(7);
        userLogin=jwtService.extractLogin(refreshToken);
        if(userLogin!=null){
            var user = this.userRepository.findByLogin(userLogin)
                    .orElseThrow();
            if(jwtService.isTokenValide(refreshToken, user)){
                var accessToken = jwtService.generateToken(user);
                //revokeAlluSER(user)
                var authResponse = AuthenticateResponse.builder()
                        .acesstoken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
