package tn.esprit.pidev.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "historiques")
public class Historique {
    @Id
    private String id;
    private  String typeAction;
    private Date dateAction;
    private String detailsAction;
    private User user;
}
