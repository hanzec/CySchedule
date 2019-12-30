package edu.iastate.coms309.cyschedulebackend.persistence.requestModel;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TokenRequest {

    @NotNull(message = "Secret should not be empty")
    String secret;

    @NotNull(message = "tokenID should not be empty")
    String tokenID;

    @NotNull(message = "refreshKey should not be empty")
    String refreshKey;
}
