package tn.esprit.pidev.Services.UserServices;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PasswordReset {

    private String newPassword ;
    private String oldPassword ;
    private String codeSent ;
    private String codeInput;
}
