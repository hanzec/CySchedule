package edu.iastate.coms309.cyschedulebackend.persistence.requestModel;


import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RegisterRequest {

    @Email(message = "Not a correct Email")
    @NotBlank(message = "Password should not empty")
    String email;

    @NotBlank(message = "Password should not empty")
    String password;

    @NotBlank(message = "Last Name should not empty")
    String lastName;

    @NotBlank(message = "Username should not empty")
    String username;

    @NotBlank(message = "First Name should not empty")
    String firstName;
}
