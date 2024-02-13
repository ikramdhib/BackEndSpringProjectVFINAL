package tn.esprit.pidev.RestControllers.AuthController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Configurations.SecurityPrincipale;
import tn.esprit.pidev.RestControllers.AuthController.Param.AuthenticateResponse;
import tn.esprit.pidev.RestControllers.AuthController.Param.AuthenticationRequest;
import tn.esprit.pidev.RestControllers.AuthController.Param.EntityResponse;
import tn.esprit.pidev.RestControllers.AuthController.Param.RegesterRequest;
import tn.esprit.pidev.Services.UserServices.AuthenticationService;
import tn.esprit.pidev.Services.UserServices.UserServiceImpl;
import tn.esprit.pidev.entities.User;

import java.io.IOException;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/auth")
@Slf4j
public class UserRestController {


    public UserServiceImpl userService;
    public AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticateResponse> register(
            @RequestBody RegesterRequest request){
        log.info("regitred");
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticateResponse> authenticate(
            @RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/is-authenticated")
    public  ResponseEntity<Object> isUserAuthenticated(){
        User principale = SecurityPrincipale.getInstance().getLoggedInPrincipal();
        if(principale!=null){
            return EntityResponse.generateResponse("Authorized", HttpStatus.OK , principale);
        }
        return EntityResponse.generateResponse("Unauthorized",HttpStatus.NOT_FOUND, false);
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> getUserProfile(){

        User user = userService.findByLoginLike(SecurityPrincipale.getInstance().getLoggedInPrincipal().login);
        return EntityResponse.generateResponse("Success",HttpStatus.OK , user);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request , HttpServletResponse response)throws IOException{
        authenticationService.refreshToken(request,response);
    }

}
