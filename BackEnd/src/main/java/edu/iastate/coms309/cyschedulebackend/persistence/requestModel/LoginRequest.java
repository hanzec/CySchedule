package edu.iastate.coms309.cyschedulebackend.persistence.requestModel;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {

    @NotBlank
    public String password;

    @NotBlank
    public String email;
}
