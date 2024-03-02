package tn.esprit.pidev.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.cglib.core.Local;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "stages")
public class Stage {
    public String id;


    public String certificate;
    public String report;
    public Date startAt;
    public Date endAt;
    public LocalDate ValidationCon;
    public Type type;
    public String nomCoach;
    public String prenomCoach;
    public String numCoach;
    public String emailCoach;
    private boolean etat;
    @DBRef
    private User user;

    @DBRef
    private User encadrant;
    @DBRef
    private Journal journal;

    private String reportDate;
    private String journalDate;
    private String attestationDate;


    // Autres méthodes de la classe Stage

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }
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
    public LocalDate getDateValidationCon() {
        return ValidationCon;
    }

    public void setDateValidationCon(LocalDate ValidationCon) {
        this.ValidationCon = ValidationCon;
    }
}
