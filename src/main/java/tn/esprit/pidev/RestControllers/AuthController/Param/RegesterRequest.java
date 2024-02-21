package tn.esprit.pidev.RestControllers.AuthController.Param;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.pidev.entities.RoleName;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegesterRequest {

    public String lastName;

    public String firstName;
    public String login;
    public String password;
    public RoleName roleName ;
}
