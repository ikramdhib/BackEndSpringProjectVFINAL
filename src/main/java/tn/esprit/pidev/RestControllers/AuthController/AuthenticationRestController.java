package tn.esprit.pidev.RestControllers.AuthController;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Configurations.JwtService;
import tn.esprit.pidev.Configurations.SecurityPrincipale;
import tn.esprit.pidev.RestControllers.AuthController.Param.*;
import tn.esprit.pidev.Services.ReclamationService.WebSocketServices.NotificationService;
import tn.esprit.pidev.Services.UserServices.AuthenticationService;
import tn.esprit.pidev.Services.UserServices.UserListnner.MailingForgetPassListner;
import tn.esprit.pidev.Services.UserServices.UserServiceImpl;
import tn.esprit.pidev.entities.User;

import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthenticationRestController {


    public UserServiceImpl userService;
    public AuthenticationService authenticationService;
    public MailingForgetPassListner mailingForgetPassListner;
    private JwtService jwtService;
    public ServletContext context;

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
        log.info("hello from refresh token it work or not ?");
        authenticationService.refreshToken(request,response);
    }




    @PostMapping("/password-reset-request")
    public ResponseEntity<?> resetPasswordRequest(@RequestBody PasswordResetUtil passwordResetRequest
                                       ) throws MessagingException, UnsupportedEncodingException {
        User user = userService.findByLoginLike(
                passwordResetRequest.getLogin());
        String passwordRrl ="";

        if(user!=null){

            String passwordResetToken = userService.generateToken(user);

            boolean isTokenValid= userService.createPasswordResetToken(passwordResetToken , user);
            if(isTokenValid){
                passwordRrl=passwordResetEmailLink(user,passwordResetToken);
            }

            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("YOU CANT GET A LINK");
    }
    public String passwordResetEmailLink(User user  ,
                                         String passwordToken) throws MessagingException, UnsupportedEncodingException {
        String url = "http://localhost:4200/ChangePassword?token="
                +passwordToken;
        mailingForgetPassListner.sendPasswordResetVerification(url ,user);
        log.info("Click the link to reset your password :  {}", url);
        return url;
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> restPassword(@RequestBody PasswordResetUtil passwordResetUtil,
                               @RequestParam("token") String token){
        User user =
                userService.findByLoginLike(jwtService.extractLogin(token));


        if(user!=null){
            userService.changePassword(user, passwordResetUtil.getNewPassword());
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CONNOT UPDATE PASSWORD");
    }


    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"
                +request.getServerPort()+request.getContextPath();
    }
    @GetMapping("/image/{image}")
    public byte[] getPhoto(@PathVariable("image") String image) throws Exception{

        return Files.readAllBytes(Paths.get(context.getRealPath("/images/")+image));
    }


}
