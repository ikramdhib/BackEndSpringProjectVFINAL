package tn.esprit.pidev.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "demandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Demande {

    @Id
    private String id;

    private String titre;

    private String description;

    private String etat;

    private String studentName;

    private String studentEmail;

    private String cvPath;  // File path or content for CV

    private String lettreMotivation;  // File path or content for lettre de motivation


    @CreatedDate
    private LocalDateTime createdAt;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @DBRef
    private User user;

    @DBRef
    private Offre offre;



}
