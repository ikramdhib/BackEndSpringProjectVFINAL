package tn.esprit.pidev.RestControllers.AuthController;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Configurations.JwtService;
import tn.esprit.pidev.Configurations.SecurityPrincipale;
import tn.esprit.pidev.RestControllers.AuthController.Param.*;
import tn.esprit.pidev.Services.UserServices.AuthenticationService;
import tn.esprit.pidev.Services.UserServices.UserListnner.MailingForgetPassListner;
import tn.esprit.pidev.Services.UserServices.UserServiceImpl;
import tn.esprit.pidev.entities.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

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




    @PostMapping("/password-reset-request")
    public String resetPasswordRequest(@RequestBody PasswordResetUtil passwordResetRequest ,
                                       HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        User user = userService.findByLoginLike(
                passwordResetRequest.getLogin());
        String passwordRrl ="";

        if(user!=null){

            String passwordResetToken = userService.generateToken(user);

            boolean isTokenValid= userService.createPasswordResetToken(passwordResetToken , user);
            if(isTokenValid){
                passwordRrl=passwordResetEmailLink(user,applicationUrl(request),passwordResetToken);
            }
        }
        return passwordRrl;
    }
    public String passwordResetEmailLink(User user , String appUrl ,
                                         String passwordToken) throws MessagingException, UnsupportedEncodingException {
        String url = appUrl+"/api/v1/auth/reset-password?token="
                +passwordToken;
        mailingForgetPassListner.sendPasswordResetVerification(url ,user);
        log.info("Click the link to reset your password :  {}", url);
        return url;
    }

    @PostMapping("/reset-password")
    public String restPassword(@RequestBody PasswordResetUtil passwordResetUtil,
                               @RequestParam("token") String token){
        User user =
                userService.findByLoginLike(jwtService.extractLogin(token));


        if(user!=null){
            userService.changePassword(user, passwordResetUtil.getNewPassword());
            return "password reset success";
        }
        return "Invalid password reset token";
    }


    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"
                +request.getServerPort()+request.getContextPath();
    }

}
