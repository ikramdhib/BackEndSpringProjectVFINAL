package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Services.ReponseServiceImpl;
import tn.esprit.pidev.Services.UserServiceImpl;
import tn.esprit.pidev.entities.Question;
import tn.esprit.pidev.entities.User;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class UserRestController {
    public UserServiceImpl userService;
    private ReponseServiceImpl reponseService;
    @PostMapping("/adduser")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }


}
