package tn.esprit.pidev.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "users")
public class User {
    public String id;
    public String lastName;
    public String firstName;
    public String login;
    public String password;
    public String resume;
    public String pic;
    public int unvId;
    public Level level;
    public String phoneNumber;
    public String emailPro;
    public String company;
    //@DBRef
  // private Role role;
    @DBRef
    private List<Stage> stage;


    public User(String id, String firstName, String lastName) {
      this.id=id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
    }

    public void setStages(List<Stage> stages) {
        this.stage = stages;
    }
    public List<Stage> getStages() {
        return stage;
    }
}

