package tn.esprit.pidev.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    public String attestation;
    public String rapport;
    public String demandeS;
    public String lettreAffect;
    public String nomSociete;
    public String numSociete;
    public String emailSociete;
    public String nomCoach;
    public String prenomCoach;
    public String numCoach;
    public String emailCoach;
    public Date startAt;
    public Date endAt;
    public Type type;

    private String reportDate;
    private String journalDate;
    private String attestationDate;

    @DBRef
    private User user; // Relation many-to-one vers User

    @DBRef
    private Journal journal;
}
