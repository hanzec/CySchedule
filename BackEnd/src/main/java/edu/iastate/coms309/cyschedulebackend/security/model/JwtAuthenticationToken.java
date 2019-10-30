package edu.iastate.coms309.cyschedulebackend.security.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.jwt.Jwt;

import java.util.Collection;

public class JwtToken implements java.io.Serializable, java.security.Principal, Authentication, CredentialsContainer {

    Jwt
    public JwtToken(String token){
        jwt.
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void eraseCredentials() {

    }
}
