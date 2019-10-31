package edu.iastate.coms309.cyschedulebackend.exception.auth;

import org.springframework.security.core.AuthenticationException;

public class TokenAlreadyExpireException extends AuthenticationException {
    public TokenAlreadyExpireException(){super("Token is already expired");}
}
