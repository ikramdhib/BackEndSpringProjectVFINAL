package tn.esprit.pidev.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
@ToString
@Builder
@Document(collection = "users")
public class User  implements UserDetails {
    public String id;
    @Size(max = 20)
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
    public String cin ;
    public String address;
    public boolean activated = false ;
    public RoleName role ;

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
}
