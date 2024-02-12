package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Services.UserServiceImpl;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.User;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserRestController {
    public UserServiceImpl userService;
    @GetMapping("/Gettt")
    public ResponseEntity<List<User>> getStudentsWithStage() {
        List<User> studentsWithStage = userService.getStudentsWithStage();
        if (studentsWithStage.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(studentsWithStage, HttpStatus.OK);
    }
    @GetMapping("/users-with-stages")
    public List<User> getUsersWithStages() {
        return userService.getUsersWithStages();
    }
    @GetMapping("/withStages")
    public ResponseEntity<List<User>> getUsersWithStagess() {
        List<User> usersWithStages = userService.getUsersWithStages();
        if (usersWithStages.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(usersWithStages, HttpStatus.OK);
    }


}
