package tn.esprit.pidev.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "stages")
public class Stage {
    public String id;
    public String journal;
    public String attestation;
    public String certificate;
    public String report;
    public String startAt;
    public String endAt;
    public Type type;
    public String nomCoach;
    public String prenomCoach;
    public String numCoach;
    public String emailCoach;
    private boolean etat;
    @DBRef
    private User user;

    @DBRef
    private User encadrant;
    public boolean isEtat() {
        return etat;
    }
    public void addEncadrant(User encadrant) {
        this.encadrant = encadrant;
    }

}
