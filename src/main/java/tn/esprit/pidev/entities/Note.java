package tn.esprit.pidev.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "notes")
public class Note {
    @Id
    public String id;
    public int crit_Mot_Aut;
    public int crit_Com_Tech;
    public int crit_Com_Commu;
    public int crit_GestT_Apprentiss;
    public int crit_Profess_QualiT;
    public int note_rdv_pedag;
    public int note_apprec_glob;
    public double note_encadr_academ;
    public int note_expert;
    public int note_encadr_profess;
    public double note_finale;

    @DBRef
    private User encadrant; // Référence vers l'encadrant

    @DBRef
    private User student; // Référence vers l'étudiant

    public int getNote_rdv_pedag() {
        return note_rdv_pedag;
    }

    public int getNote_apprec_glob() {
        return note_apprec_glob;
    }

    public void setNote_apprec_glob(int note_apprec_glob) {
        this.note_apprec_glob = note_apprec_glob;
    }
}
