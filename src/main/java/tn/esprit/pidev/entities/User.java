package tn.esprit.pidev.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "users")
public class User {
    public String _id;
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
    @DBRef
    private Offre offre;

}
