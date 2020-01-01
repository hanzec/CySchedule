package edu.iastate.coms309.cyschedulebackend.exception.auth.request;

import org.springframework.security.core.AuthenticationException;

public class RequestAlreadyExpireException extends AuthenticationException {
    public RequestAlreadyExpireException(){super("Token is already expired");}
}
