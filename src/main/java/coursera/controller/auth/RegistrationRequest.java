package coursera.controller.auth;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class RegistrationRequest {
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;

    private String name;
    private String surname;
    private String middleName;
}
