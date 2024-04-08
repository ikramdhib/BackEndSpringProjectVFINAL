package tn.esprit.pidev.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "stages")
public class Stage {
    @Id

    public String id;


    public String certificate;
    public String report;

    public String attestation;
    public String rapport;
    public String demandeS;
    public String lettreAffect;
    public String nomSociete;
    public String numSociete;
    public String emailSociete;
    public String nomCoach;
    public String prenomCoach;
    public String numCoach;
    public String emailCoach;
    public Date startAt;
    public Date endAt;
    public LocalDate ValidationCon;
    public Type type;
    private boolean etat;

    @JsonBackReference
    @DBRef
    private User user;

    @DBRef
    private User encadrant;
    @DBRef
    private Journal journal;

    private String reportDate;
    private String journalDate;
    private String attestationDate;
    public byte[] rapportPdf;

    private boolean rapportExiste;
    @DBRef
    private User serviceStage;


    // Autres méthodes de la classe Stage

    public boolean isEtat() {
        return etat;
    }

    public void addEncadrant(User encadrant) {
        this.encadrant = encadrant;
    }

    private byte[] attestationPdf;

    public void setAttestationPdf(byte[] attestationPdf) {
        this.attestationPdf = attestationPdf;
    }

    public byte[] getAttestationPdf() {
        return attestationPdf;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Stage stage = (Stage) obj;
        return Objects.equals(id, stage.id);
    }



}
