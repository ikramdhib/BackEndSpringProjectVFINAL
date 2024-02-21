package tn.esprit.pidev.Services.UserServices;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.User;

import tn.esprit.pidev.entities.RoleName;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements IServiceUser {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
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
}
