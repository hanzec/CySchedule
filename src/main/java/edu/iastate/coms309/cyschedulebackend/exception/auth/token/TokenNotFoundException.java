package edu.iastate.coms309.cyschedulebackend.exception.auth.token;

import org.springframework.security.core.AuthenticationException;

public class TokenNotFoundException extends AuthenticationException {
    public TokenNotFoundException(){super("Token is already expired");}
}
