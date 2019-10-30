package edu.iastate.coms309.cyschedulebackend.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JwtTokenProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken j
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(JwtAuthenticationToken.class);
    }
}
