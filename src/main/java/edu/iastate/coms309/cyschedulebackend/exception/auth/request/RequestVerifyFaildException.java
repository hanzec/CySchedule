package edu.iastate.coms309.cyschedulebackend.exception.auth.request;

import org.springframework.security.core.AuthenticationException;

public class RequestVerifyFaildException extends AuthenticationException {
    public RequestVerifyFaildException(){super("Token cannot verified");}
}
