package edu.iastate.coms309.cyschedulebackend.exception.auth;

import org.springframework.security.core.AuthenticationException;

public class TokenNotCorrectUrlException extends AuthenticationException {
    public TokenNotCorrectUrlException(){super("Token does not contain correct endpoint");}
}
