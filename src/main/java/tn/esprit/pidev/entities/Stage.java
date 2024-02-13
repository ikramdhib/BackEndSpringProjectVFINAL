package tn.esprit.pidev.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "stages")
public class Stage {
    @Id
    public String id;
    public String journal;
    public String certificate;
    public String report;
    public String nomSociete;
    public String numSociete;
    public String emailSociete;
    public String nomCoach;
    public String prenomCoach;
    public String numCoach;
    public String emailCoach;
    public String startAt;
    public String endAt;
    public Type type;

    @DBRef
    private User user; // Relation many-to-one vers User
}
