package tn.esprit.pidev.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "offres")
public class Offre {


    @Id
    private String id;

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
    private int likes ;
    private int dislikes;
    private String lienLinkedIn; // Nouvel attribut pour le lien LinkedIn

    @JsonIgnore
    @DBRef
    private List<User> likedBy = new ArrayList<>();

    @JsonIgnore
    @DBRef
    private List<User> dislikedBy = new ArrayList<>();


    @DBRef
    private User user;

    private List<String> hashtags;

    @JsonIgnore
    @DBRef
    private List<Commentaire> commentaires;

    public String getLienLinkedIn() {
        return lienLinkedIn;
    }

    public void setLienLinkedIn(String lienLinkedIn) {
        this.lienLinkedIn = lienLinkedIn;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }





    // Méthodes like et dislike
    public void like(User user) {
        likedBy.add(user);
        dislikedBy.remove(user); // Assurez-vous qu'un utilisateur ne peut pas à la fois aimer et ne pas aimer une offre
    }

    public void dislike(User user) {
        dislikedBy.add(user);
        likedBy.remove(user); // Assurez-vous qu'un utilisateur ne peut pas à la fois aimer et ne pas aimer une offre
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public List<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }

    public Type getType() {
        return type;
    }


    public void setType(Type type) {
        this.type = type;
    }

    public Number getDuree() {
        return duree;
    }

    public void setDuree(Number duree) {
        this.duree = duree;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    private String imageUrl;

    public String getImage() {
        return imageUrl;
    }

    public void setImage(String image) {
        this.imageUrl = image;
    }

    public String get_id() {
        return id;
    }

    public void set_id(String _id) {
        this.id = _id;
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
                "_id='" + id + '\'' +
                ", nomEntreprise='" + nomEntreprise + '\'' +
                ", nomEncadrant='" + nomEncadrant + '\'' +
                ", prenomEncadrant='" + prenomEncadrant + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", datedebut_stage=" + datedebut_stage +
                ", datefin_stage=" + datefin_stage +
                ", duree=" + duree +
                ", user=" + user +
                ", imageUrl='" + imageUrl + '\'' +
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