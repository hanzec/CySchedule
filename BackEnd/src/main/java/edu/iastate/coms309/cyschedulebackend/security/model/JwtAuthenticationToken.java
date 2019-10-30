package edu.iastate.coms309.cyschedulebackend.security.model;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sun.xml.bind.v2.TODO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;

public class JwtAuthenticationToken implements java.io.Serializable, java.security.Principal, Authentication, CredentialsContainer {

    private DecodedJWT jwt = null;
    private Object credentials = null;
    private boolean authenticated = false;
    private Object authenticationDetails = null;
    private Collection<? extends GrantedAuthority> authorities = null;

    public JwtAuthenticationToken(String token){
        this(token,new ArrayList<>());
    }

    public JwtAuthenticationToken(String token,
                                  Collection<? extends GrantedAuthority> authorities){
        this(token,token,authorities);
    }

    public JwtAuthenticationToken(String token,
                                  Object credentials,
                                  Collection<? extends GrantedAuthority> authorities){

        this.jwt = JWT.decode(token);
        this.credentials = credentials;
        this.authorities = authorities;
    }


    @Override
    public void eraseCredentials() {}

    @Override
    public Object getPrincipal() {
        return jwt.getId();
    }

    @Override
    public String getName() { return jwt.getSubject(); }

    @Override
    public Object getCredentials() { return credentials; }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public Object getDetails() {
        return authenticationDetails;
    }

    public void setDetails(Object details){this.authenticationDetails = details;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException { authenticated = b; }
}
