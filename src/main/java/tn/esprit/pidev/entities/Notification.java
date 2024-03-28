package tn.esprit.pidev.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "notifications")
public class Notification {
    @Id
    private String id ;
    private String userId;
    private String message;
    private boolean isRead;
    private String questionId;
}
