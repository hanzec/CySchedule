package edu.iastate.coms309.cyschedulebackend.persistence.requestModel;

import lombok.Data;

@Data
public class LoginRequest {

    String email;

    String password;

    String lastName;

    String username;

    String firstName;
}
