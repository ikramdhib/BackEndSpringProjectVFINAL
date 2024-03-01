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
@Document(collection = "taches")
public class Tache {
    @Id
    public String id;
    public Date date;
    public String libelle;
    public Boolean status;
    @DBRef
    public Journal journal;

}
