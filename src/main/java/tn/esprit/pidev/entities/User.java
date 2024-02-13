package tn.esprit.pidev.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "users")
public class User {
    @Id
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

    public RoleName roleName;

    @DBRef
    private List<Stage> stages; // Relation one-to-many vers Stage
}
