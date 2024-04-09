package tn.esprit.pidev.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "Commentoffres")
public class Commentoffres {
    @Id
    private String id;
    private String texte;

    @JsonIgnore
    @DBRef
    private Offre offre;
    @JsonIgnore
    @DBRef
    private User user;
}