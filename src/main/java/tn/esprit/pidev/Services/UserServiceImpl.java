package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.StageRepository;
import tn.esprit.pidev.Repositories.UserRepository;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IServiceUser{
    public UserRepository userRepository;
    public StageRepository stageRepository;

    public List<User> getStudentsBySupervisor(String encadrantId) {
        User encadrant = userRepository.findById(encadrantId)
                .orElseThrow(() -> new IllegalArgumentException("Encadrant with ID " + encadrantId + " not found"));

        List<Stage> stages = stageRepository.findByEncadrant(encadrant);
        List<User> students = stages.stream()
                .map(stage -> {
                    User student = stage.getUser();
                    User studentDTO = new User();
                    studentDTO.setId(student.getId());
                    studentDTO.setFirstName(student.getFirstName());
                    studentDTO.setLastName(student.getLastName());
                    studentDTO.setLogin(student.getLogin()); // Ajout du login de l'étudiant
                    studentDTO.setPhoneNumber(student.getPhoneNumber()); // Ajout du numéro de téléphone de l'étudiant
                    studentDTO.setStageId(stage.getId());
                    return studentDTO;
                })
                .collect(Collectors.toList());
        return students;
    }
    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    public User findUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }
    public Date getStageStartDate(String userId, String stageId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getStages() != null) {
                for (Stage stage : user.getStages()) {
                    if (stage.getId().equals(stageId)) {
                        return stage.getStartAt();
                    }
                }
            }
        }
        return null; // Stage non trouvé
    }

}


