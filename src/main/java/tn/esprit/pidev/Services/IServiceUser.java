package tn.esprit.pidev.Services;

import org.bson.types.ObjectId;
import tn.esprit.pidev.entities.Stage;
import tn.esprit.pidev.entities.User;

import java.util.List;

public interface IServiceUser {
    public List<User> getStudentsBySupervisor(String encadrantId);
}
