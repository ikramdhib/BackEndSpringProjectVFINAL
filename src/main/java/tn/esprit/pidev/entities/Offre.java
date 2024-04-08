package tn.esprit.pidev.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "offres")
public class Offre {


    @Id
    private String _id;

    private String nomEntreprise;

    private String nomEncadrant;
    private String prenomEncadrant;
    private String email;
    private String description;

    private Type type;
    @DateTimeFormat
    private Date datedebut_stage;
    @DateTimeFormat

    private Date datefin_stage;
    private Number duree;
    @DBRef
    private User user;

    private String imageUrl;

    public String getImage() {
        return imageUrl;
    }

    public void setImage(String image) {
        this.imageUrl = image;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNomEntreprise() {
        return nomEntreprise;
    }

    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    @Override
    public String toString() {
        return "Offre{" +
                "_id='" + _id + '\'' +
                ", nomEntreprise='" + nomEntreprise + '\'' +
                 '\'' +
                ", nomEncadrant='" + nomEncadrant + '\'' +
                ", prenomEncadrant='" + prenomEncadrant + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", datedebut_stage=" + datedebut_stage +
                ", datefin_stage=" + datefin_stage +
                '}';
    }




    public String getNomEncadrant() {
        return nomEncadrant;
    }

    public void setNomEncadrant(String nomEncadrant) {
        this.nomEncadrant = nomEncadrant;
    }

    public String getPrenomEncadrant() {
        return prenomEncadrant;
    }

    public void setPrenomEncadrant(String prenomEncadrant) {
        this.prenomEncadrant = prenomEncadrant;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatedebut_stage() {
        return datedebut_stage;
    }

    public void setDatedebut_stage(Date datedebut_stage) {
        this.datedebut_stage = datedebut_stage;
    }

    public Date getDatefin_stage() {
        return datefin_stage;
    }

    public void setDatefin_stage(Date datefin_stage) {
        this.datefin_stage = datefin_stage;
    }

}
