package edu.iastate.coms309.cyschedulebackend.persistence.requestModel;


import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class RegisterRequest {

    @Email(message = "Not a correct Email")
    @NotNull(message = "Password should not empty")
    String email;

    @NotNull(message = "Password should not empty")
    String password;

    @NotNull(message = "Last Name should not empty")
    String lastName;

    @NotNull(message = "Username should not empty")
    String username;

    @NotNull(message = "First Name should not empty")
    String firstName;
}
