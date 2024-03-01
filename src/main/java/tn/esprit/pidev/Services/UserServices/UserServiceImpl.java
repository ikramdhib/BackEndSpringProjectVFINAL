package tn.esprit.pidev.Services.UserServices;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Configurations.JwtService;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.Services.UserServices.UserListnner.MailingForgetPassListner;
import tn.esprit.pidev.entities.User;

import tn.esprit.pidev.entities.RoleName;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements IServiceUser {

    public CloudinaryService cloudinaryService;

    private  MailingForgetPassListner mailingForgetPassListner;
    private  UserRepository userRepository;
    private  PasswordEncoder passwordEncoder;
    @Override
    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updatePassword(String id, String newPass)  {

        User user = userRepository.findById(id).orElse(null);
        if(user!=null){
            user.setPassword(passwordEncoder.encode(newPass));
        }
        return userRepository.save(user);
    }


    public String confirmUpdatePass(String id, String newPass , String oldPass) throws MessagingException, UnsupportedEncodingException {

        User user = userRepository.findById(id).orElse(null);

        StringBuilder codeBuilder = new StringBuilder();
        if(user!=null){
            log.info("user1");
            if(passwordEncoder.matches(newPass,user.getPassword())
            || !passwordEncoder.matches(oldPass,user.getPassword())){
                log.info("trueeee1");
                return null;
            }
            //generer le code de verification
            Random random = new Random();

            // Générer un code de 6 chiffres
            for (int i = 0; i < 6; i++) {
                int digit = random.nextInt(10); // Générer un chiffre aléatoire entre 0 et 9
                codeBuilder.append(digit); // Ajouter le chiffre à la chaîne de caractères
            }
            log.info("trueeee2");
            mailingForgetPassListner.sendVerificationEmail(user, codeBuilder);


        }
        return codeBuilder.toString() ;
    }



    private final JwtService jwtService;
    private final Path rootLocation = Paths.get("images");

    public User findByLoginLike(String username) {
        return userRepository.findByLoginLike(username);
    }
    @Override
    public User addStudentUser(User user) {

      return userRepository.save( User.builder()
                .role(RoleName.ETUDIANT)
                .login(user.login)
                .password(passwordEncoder.encode(user.password))
                .pic(user.pic)
                      .firstName(user.firstName)
                      .lastName(user.lastName)
                      .activated(true)
                      .address(user.address)
                      .cin(user.cin)
                      .level(user.level)
                      .unvId(user.unvId)
                      .phoneNumber(user.phoneNumber)
                .build());
    }

    @Override
    public String saveImageForUsers(MultipartFile file) throws IOException {
        if(file.isEmpty()){
            throw new IOException("l'image est null");
        }
        String filePath = file.getOriginalFilename();
        log.info(filePath+"--------------------++++++++++++++++++");
        String fileExtention = filePath.substring(filePath.lastIndexOf("."));
        String fileName = UUID.randomUUID()+fileExtention;

        Path fileDestination = this.rootLocation
                .resolve(fileName).normalize().toAbsolutePath();


        if(!fileDestination.getParent().equals(this.rootLocation.toAbsolutePath())){
            throw new IOException("mouvaise destination");
        }

        try(InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream , fileDestination , StandardCopyOption.REPLACE_EXISTING);
        }

        String fileUrl = "/images/"+fileName;
        return fileUrl;
    }

    @Override
    public User addSupervisor(User user) {
        return userRepository.save(
                User.builder()
                        .lastName(user.lastName)
                        .firstName(user.firstName)
                        .address(user.address)
                        .login(user.login)
                        .password(passwordEncoder.encode(user.password))
                        .phoneNumber(user.phoneNumber)
                        .role(RoleName.ENCADRANT)
                        .activated(false)
                        .emailPro(user.emailPro)
                        .company(user.company)
                        .pic(user.pic)
                        .cin(user.cin)
                        .build()
        );
    }

    @Override
    public Boolean blockUser(String id) {
        User user = userRepository.findById(id).orElse(null);

        if(user.isActivated()){
            user.setActivated(false);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public List<User> getAllUserWithRole(RoleName roleName) {

        return userRepository.findByRole(roleName);
    }
    @Override
    public boolean createPasswordResetToken(String token , UserDetails userDetails){
        return jwtService.isTokenValide(token,userDetails);
    }
    @Override
    public String generateToken(User user){
        return jwtService.generatePasswordRessetToken(user);
    }
    @Override
    public void changePassword(User user , String newPassword){
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public User updateUser(String id , User user) {
        User user1 = userRepository.findById(id).orElse(null);

        if(user1.role.equals(RoleName.ENCADRANT)){
            if(user.pic!=null){
                try {
                    cloudinaryService.delete(user.pic);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            user1.setAddress(user.address);
            user1.setPhoneNumber(user.phoneNumber);
            user1.setPic(user.pic);
            user1.setEmailPro(user.emailPro);
            user1.setCin(user.cin);
        }else{
            if(user.pic!=null){
                try {
                    cloudinaryService.delete(user.pic);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            user1.setAddress(user.address);
            user1.setPhoneNumber(user.phoneNumber);
            user1.setCin(user.cin);
            user1.setPic(user.pic);
        }
        log.info(user1.pic);
        return userRepository.save(user1);
    }

}
