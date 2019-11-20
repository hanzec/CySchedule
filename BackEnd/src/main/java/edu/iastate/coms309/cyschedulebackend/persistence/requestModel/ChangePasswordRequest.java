package edu.iastate.coms309.cyschedulebackend.persistence.requestModel;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ChangePasswordRequest {

    @NotEmpty(message = "Request should contains a new password")
    String newPassword;

    @NotEmpty(message = "Request should provide old password")
    String oldPassword;
}
