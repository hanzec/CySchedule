package edu.iastate.coms309.cyschedulebackend.exception.auth.request;

import org.springframework.security.core.AuthenticationException;

public class RequestIncorrectException extends AuthenticationException {
    public RequestIncorrectException(){super("Token does not contain correct endpoint");}
}
