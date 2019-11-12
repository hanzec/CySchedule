package edu.iastate.coms309.cyschedulebackend.exception.event;

import javax.naming.AuthenticationException;

public class EventNotFoundException extends AuthenticationException {
    public EventNotFoundException(String eventID){super("Event with id: " + eventID + "is not found");}

}
