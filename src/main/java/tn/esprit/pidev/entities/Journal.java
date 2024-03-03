package tn.esprit.pidev.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "journals")
public class Journal {
    @Id
    public String id;
    @DBRef
    public List<Tache> taches;
    @JsonBackReference
    @DBRef
    public Stage stage;
    public Stream<Tache> streamTasks() {
        return taches.stream();
    }
    public List<Tache> getTaches() {
        return taches;
    }
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Journal journal = (Journal) obj;
        return Objects.equals(id, journal.id);
    }
}
