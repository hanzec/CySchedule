package edu.iastate.coms309.cyschedulebackend.exception.event;

import javax.naming.AuthenticationException;

public class NotPrimitiveException extends AuthenticationException {
    private NotPrimitiveException(String id){super("You don't have permission for Event : " + id);}
}
