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
@CrossOrigin(origins = "http://localhost:4200")

public class UserRestController {
    public UserServiceImpl userService;
    @GetMapping("/students/{encadrantId}")
    public List<User> getStudentsBySupervisor(@PathVariable String encadrantId) {
        return userService.getStudentsBySupervisor(encadrantId);
    }


}
