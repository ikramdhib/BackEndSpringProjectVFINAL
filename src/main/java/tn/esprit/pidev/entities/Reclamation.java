package tn.esprit.pidev.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "reclamations")
public class Reclamation {
    public String id;
    public String from;
    public String to;
   // public String body;
   // public Objectif objective;
    //public User user;
   // public boolean seen ;
   // public Date createdAt;
}
