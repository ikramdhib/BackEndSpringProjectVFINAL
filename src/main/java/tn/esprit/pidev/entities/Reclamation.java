package tn.esprit.pidev.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "reclamations")
public class Reclamation {
    public int id;
    public String from;
    public String to;
    public String body;
    public Objectif objective;
}
