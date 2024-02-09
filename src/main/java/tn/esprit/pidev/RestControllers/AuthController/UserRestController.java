package tn.esprit.pidev.RestControllers.AuthController;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.pidev.Services.UserServiceImpl;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserRestController {


    public UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticateResponse> register(
            @RequestBody RegesterRequest request){

        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticateResponse> authenticate(
            @RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(userService.authenticate(request));
    }


}
