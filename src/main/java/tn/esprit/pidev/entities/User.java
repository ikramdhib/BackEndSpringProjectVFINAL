package tn.esprit.pidev.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
@Builder
@Document(collection = "users")
public class User  implements UserDetails {
    @Id
    public String id;
    public String lastName;
    @Size(max = 20)
    public String firstName;
    @Email
    @NotBlank
    @Size(max = 50)
    @Indexed(unique = true)
    public String login;
    @NotBlank
    public String password;
    public String resume;
    public String pic;
    public String unvId;
    public Level level;
    public String phoneNumber;
    @Email
    @Size(max = 50)
    @Indexed(unique = true)
    public String emailPro;
    public String company;
    @Size(max = 8)
    public String cin;
    public String address;
    public RoleName role;
    public boolean activated = false;
    public boolean validated;
    public Date createdAt;
    @JsonBackReference
    @DBRef
    private List<Note> notes;

    //@DBRef
    // private Role role;
    @DBRef
    private List<Stage> stage;
    private String stageId;

    @DBRef
    private Offre offre;

    @DBRef
    private List<Commentaire> commentaires;

    @DBRef
    private List<Demande> demandes;

    @DBRef
    private List<Event>  events;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }



    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



        public String getStageId () {
            return stageId;
        }

        public void setStageId (String stageId){
            this.stageId = stageId;
        }



        public void setStages (List < Stage > stages) {
            this.stage = stages;
        }
        public List<Stage> getStages () {
            return stage;
        }



}

