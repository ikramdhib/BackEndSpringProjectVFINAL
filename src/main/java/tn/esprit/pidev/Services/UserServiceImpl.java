package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.StageRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IServiceUser{
    public UserRepository userRepository;
    public StageRepository stageRepository;

    public List<User> getStudentsBySupervisor(String encadrantId) {
        User encadrant = userRepository.findById(encadrantId).orElse(null);
        if (encadrant == null) {
            // Gérer le cas où l'encadrant n'est pas trouvé, par exemple en lançant une exception
            throw new IllegalArgumentException("Encadrant with ID " + encadrantId + " not found");
        }

        List<Stage> stages = stageRepository.findByEncadrant(encadrant);
        return stages.stream()
                .map(Stage::getUser)
                .toList();
    }
}


