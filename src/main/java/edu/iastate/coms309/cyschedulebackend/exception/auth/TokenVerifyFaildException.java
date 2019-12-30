package edu.iastate.coms309.cyschedulebackend.exception.auth;

import org.springframework.security.core.AuthenticationException;

public class TokenVerifyFaildException extends AuthenticationException {
    public TokenVerifyFaildException(){super("Token cannot verified");}
}
