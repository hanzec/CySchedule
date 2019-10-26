package edu.iastate.coms309.cyschedulebackend.persistence.requestModel;

import lombok.Data;

@Data
public class LoginRequest {

    public String password;

    public String email;
}
