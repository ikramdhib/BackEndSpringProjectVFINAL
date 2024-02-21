package tn.esprit.pidev.RestControllers.AuthController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Services.UserServices.UserServiceImpl;
import tn.esprit.pidev.entities.Level;
import tn.esprit.pidev.entities.User;

import java.io.IOException;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/user")
@Slf4j
public class UserRestController {

    public UserServiceImpl userService;

    @PostMapping("/addStudent")
    public ResponseEntity<?> addStudentUser(@RequestParam(value = "file" , required = false)MultipartFile file ,
                                            @RequestParam("login") String login ,
                                            @RequestParam("password") String password ,
                                            @RequestParam("lastName") String lastName,
                                            @RequestParam("firstName") String firstName,
                                            @RequestParam("phoneNumber") String phoneNumber ,
                                            @RequestParam("address") String address,
                                            @RequestParam("cin") String cin ,
                                            @RequestParam("level") Level level,
                                            @RequestParam("unvId") String unvId) throws IOException {
        try {
            User user = new User();
            user.setLogin(login);
            user.setPassword(password);
            user.setCin(cin);
            user.setAddress(address);
            user.setLevel(level);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhoneNumber(phoneNumber);
            user.setUnvId(unvId);

            if (!file.isEmpty()) {
                String fileUrl = userService.saveImageForUsers(file);
                user.setPic(fileUrl);
            }

            User user1 = userService.addStudentUser(user);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(user1);

        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("bad request");
        }
    }
}
