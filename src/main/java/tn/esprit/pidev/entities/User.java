package tn.esprit.pidev.entities;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
@ToString
@Document(collection = "users")
public class User {
    public int id;
    public String lastName;
    public String firstName;
    public String login;
    public String password;
    public String resume;
    public String pic;
    public String unvId;
    public Level level;
    public String phoneNumber;
    public String emailPro;
    public String company;
    public Role role ;
}
