package tn.esprit.pidev.RestControllers.AuthController;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.RestControllers.AuthController.Param.ResponseModel;
import tn.esprit.pidev.Services.UserServices.CloudinaryService;
import tn.esprit.pidev.Services.UserServices.PasswordReset;
import tn.esprit.pidev.Services.UserServices.UserServiceImpl;
import tn.esprit.pidev.entities.Level;
import tn.esprit.pidev.entities.RoleName;
import tn.esprit.pidev.entities.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/user")
@Slf4j
public class UserRestController {

    public UserServiceImpl userService;

    private final  ResponseModel responseModel = new ResponseModel();

    public CloudinaryService cloudinaryService ;

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


        BufferedImage bi = ImageIO.read(file.getInputStream());

        if(bi ==null){
            return new ResponseEntity<>("Image non valide!", HttpStatus.BAD_REQUEST);
        }
        Map result = cloudinaryService.upload(file);

        user.setPic((String) result.get("url"));

        User user1 = userService.addStudentUser(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(user1);


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


            BufferedImage bi = ImageIO.read(file.getInputStream());

            if(bi ==null){
                responseModel.setResponse("Image non valide!");
                return new ResponseEntity<>(responseModel, HttpStatus.BAD_REQUEST);
            }

            Map result = cloudinaryService.upload(file);

             user.setPic((String) result.get("url"));


            User user1 = userService.addSupervisor(user);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(user1);

        }catch (IOException e){
            responseModel.setResponse("BAD REQUEST");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
        }
    }

    @PutMapping("/blockUser/{id}")
    public ResponseEntity<?> blockUser(@PathVariable String id){

        if(userService.blockUser(id)){

            responseModel.setResponse("USER BLOCKED");

            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        }
        responseModel.setResponse("TRY AGAIN");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
    }
    @PutMapping("/deblockUser/{id}")
    public ResponseEntity<?> deBlockUser(@PathVariable String id){

        if(userService.deBlockUser(id)){

            responseModel.setResponse("USER DEBLOCKED");

            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        }
        responseModel.setResponse("TRY AGAIN");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
    }

    @GetMapping("/users/{roleName}")
    public ResponseEntity<?> getAllUsersWithRole(@PathVariable RoleName roleName){
        List<User> users = userService.getAllUserWithRole(roleName);
        if(!users.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(users);
        }
        responseModel.setResponse("NO AVAILABLE USERS");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModel);
    }

    @PutMapping("/updateUser/{id}") //a modifier
    public ResponseEntity<?> updateUser( @PathVariable String id , @RequestParam(value = "file" , required = false) MultipartFile file ,
                                         @RequestParam(value = "phoneNumber" , required = false) String phoneNumber ,
                                         @RequestParam(value = "address" , required = false) String address,
                                         @RequestParam(value = "cin" , required = false) String cin ,
                                         @RequestParam(value = "emailPro" , required = false) String emailPro,
                                          @RequestParam(value = "fisrtName" , required = false) String fisrtName,
                                          @RequestParam(value = "lastName" , required = false) String lastName,
                                         @RequestParam(value = "unvId" , required = false) String unvId,
                                         @RequestParam(value = "level" , required = false) Level level,
                                         @RequestParam(value = "login" , required = false) String login,
                                         @RequestParam(value = "company" , required = false) String company
    )
            throws IOException {

        User user = new User();

        if(file!=null){
            Map result = cloudinaryService.upload(file);

            user.setPic((String) result.get("url"));
        }
        user.setPhoneNumber(phoneNumber);
        user.setCin(cin);
        user.setAddress(address);
        user.setEmailPro(emailPro);
        user.setLogin(login);
        user.setUnvId(unvId);
        user.setLastName(lastName);
        user.setFirstName(fisrtName);
        user.setLevel(level);
        user.setCompany(company);


        User user1 = userService.updateUser(id,user);
        if(user1!=null){
            return ResponseEntity.status(HttpStatus.OK).body(user1);
        }

        responseModel.setResponse("CANNOT UPDATE THIS USER");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id){

        User user = userService.getUserById(id);
        if(user == null){
            responseModel.setResponse("USER NOT FOUND");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModel);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/requestOfChangingPass/{id}")
    public ResponseEntity<?> requestOfChangingPass(@PathVariable String id ,
                                                   @RequestBody PasswordReset passwordReset) throws MessagingException, UnsupportedEncodingException {

        String code = userService.confirmUpdatePass(id , passwordReset.getNewPassword() , passwordReset.getOldPassword() );
        log.info(code+"7777777777777777777777777777777777777777");
        PasswordReset passwordReset1 = new PasswordReset();
        if(code!=null){
            passwordReset1.setCodeSent(code);
            return ResponseEntity.status(HttpStatus.OK).body(passwordReset1);
        }
        responseModel.setResponse("TRY AGAIN");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
    }

    @PutMapping("/changePassword/{id}")
    public ResponseEntity<?> changePassword(
            @PathVariable String id ,
            @RequestBody PasswordReset newPasswordReset){
        log.info(newPasswordReset.getCodeInput()+"@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        log.info(newPasswordReset.getCodeSent()+"@@@@@@@@@@@@@@@@@@@@@");
        User user =null ;
        if(newPasswordReset.getCodeInput().equals(newPasswordReset.getCodeSent())){

            user = userService.updatePassword(id,newPasswordReset.getNewPassword());
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        responseModel.setResponse("TRY AGAIN");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
    }

@DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable String id){
        User user = userService.deleteUser(id);
        if(user!=null){
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        responseModel.setResponse("TRY AGAIN");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModel);
    }

    @PostMapping("/addServiceStage")
    public ResponseEntity<?> addServiceStage(@RequestBody User user){
        User user1 = userService.addServiceStage(user);
        if(user1!=null){
            return ResponseEntity.status(HttpStatus.OK).body(user1);
        }
        responseModel.setResponse("TRY AGAIN");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModel);
    }
    @PutMapping("/updateService/{id}")
    public ResponseEntity<?> updateService(@PathVariable String id ,@RequestBody User user){
        User user1 = this.userService.updateServiceStage(id,user);
        if(user1!=null){
            return ResponseEntity.status(HttpStatus.OK).body(user1);
        }
        responseModel.setResponse("TRY AGAIN");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModel);
    }


}
