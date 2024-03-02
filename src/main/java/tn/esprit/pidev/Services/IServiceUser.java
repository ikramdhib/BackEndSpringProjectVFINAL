package tn.esprit.pidev.Services;

import org.bson.types.ObjectId;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IServiceUser {
    public List<User> getStudentsBySupervisor(String encadrantId);
    public User findUserById(String userId) ;
    public Optional<User> getUserById(String userId) ;
    public Date getStageStartDate(String userId, String stageId) ;
    }

