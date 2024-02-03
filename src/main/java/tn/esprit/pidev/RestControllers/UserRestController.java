package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.pidev.Services.UserServiceImpl;
import tn.esprit.pidev.entities.User;

@RestController
@AllArgsConstructor
public class UserRestController {
    public UserServiceImpl userService;
    @PostMapping("/adduser")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }
}
