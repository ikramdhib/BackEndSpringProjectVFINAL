package tn.esprit.pidev.RestControllers.AuthController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Services.UserServices.UserServiceImpl;
import tn.esprit.pidev.entities.Level;
import tn.esprit.pidev.entities.RoleName;
import tn.esprit.pidev.entities.User;

import java.io.IOException;
import java.util.List;

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

    @PostMapping("/addSupervisor")
    public ResponseEntity<?> addSupervisorUser(@RequestParam(value = "file" , required = false) MultipartFile file ,
                                               @RequestParam("login") String login ,
                                               @RequestParam("password") String password ,
                                               @RequestParam("lastName") String lastName,
                                               @RequestParam("firstName") String firstName,
                                               @RequestParam("phoneNumber") String phoneNumber ,
                                               @RequestParam("address") String address,
                                               @RequestParam("cin") String cin ,
                                               @RequestParam("emailPro") String emailPro,
                                               @RequestParam("company") String company) throws IOException {
        try {
            User user = new User();
            user.setPhoneNumber(phoneNumber);
            user.setLastName(lastName);
            user.setFirstName(firstName);
            user.setCin(cin);
            user.setAddress(address);
            user.setLogin(login);
            user.setPassword(password);
            user.setEmailPro(emailPro);
            user.setCompany(company);

            if (!file.isEmpty()) {
                String fileUrl = userService.saveImageForUsers(file);
                user.setPic(fileUrl);
            }

            User user1 = userService.addSupervisor(user);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(user1);

        }catch (IOException e){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad Request");
        }
    }

    @PutMapping("/blockUser/{id}")
    public ResponseEntity<?> blockUser(@PathVariable String id){
        if(userService.blockUser(id)){
            return ResponseEntity.status(HttpStatus.OK).body("USER BLOCKED");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("USER NOT BLOCKE");
    }

    @GetMapping("/users/{roleName}")
    public ResponseEntity<?> getAllUsersWithRole(@PathVariable RoleName roleName){
        List<User> users = userService.getAllUserWithRole(roleName);
        if(!users.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(users);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO AVAILABLE USERS");
    }
}
