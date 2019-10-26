package edu.iastate.coms309.cyschedulebackend.security.model;

import lombok.Data;

@Data
public class TokenObject {
    String token;

    String userID;

    String tokenID;
}
