package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.Question;
import tn.esprit.pidev.entities.User;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IServiceUser{
    public UserRepository userRepository;
    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

}
