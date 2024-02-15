package tn.esprit.pidev.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "questions")
public class Question {
    @Id
    public String id;
    public String titre;
    public String content;
}
