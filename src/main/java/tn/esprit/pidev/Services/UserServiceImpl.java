package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IServiceUser{
    public UserRepository userRepository;
    public List<User> getUsersWithStages() {
        List<User> usersWithStages = userRepository.findAll(); // ou une autre méthode de récupération
        return usersWithStages.stream()
                .map(this::mapUserToUserDtoWithStages)
                .collect(Collectors.toList());
    }

    private User mapUserToUserDtoWithStages(User user) {
        User userDto = new User(user.getId(), user.getFirstName(), user.getLastName());
        userDto.setStages(user.getStages());
        return userDto;
    }

    @Override
    public List<User> getStudentsWithStage() {
        return null;
    }

    public List<User> getUsersWithStageData() {
        return userRepository.findAll();
    }

    public List<User> getUsersWithStagess() {
        List<User> usersWithStages = userRepository.findAll(); // Assurez-vous que UserRepository prend en charge le chargement paresseux des relations
        return usersWithStages.stream()
                .peek(user -> user.getStages().size()) // Force le chargement des stages (lazy loading)
                .collect(Collectors.toList());
    }
  }
