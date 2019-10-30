package edu.iastate.coms309.cyschedulebackend.security.provider;

import edu.iastate.coms309.cyschedulebackend.Service.EventService;
import edu.iastate.coms309.cyschedulebackend.security.model.JwtAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JwtTokenAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    EventService eventService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken credential = (JwtAuthenticationToken) authentication;


        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(JwtAuthenticationToken.class);
    }
}
