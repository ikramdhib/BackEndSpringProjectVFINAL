package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Services.UserServiceImpl;
import tn.esprit.pidev.entities.User;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600 , allowCredentials = "true")
@RequestMapping("/api/user")
public class UserRestController {
    public UserServiceImpl userService;

    @GetMapping("afficherUtilisateur/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
}
