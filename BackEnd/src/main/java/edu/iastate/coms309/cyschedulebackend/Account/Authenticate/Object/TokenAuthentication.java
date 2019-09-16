package edu.iastate.coms309.cyschedulebackend.Account.Authenticate.Object;

import edu.iastate.coms309.cyschedulebackend.persistence.model.UserToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class TokenAuthentication implements Authentication {

    String userID;
    UserToken token;
    boolean authenticatedStatus = false;

    public TokenAuthentication(String userID, UserToken userToken){

    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return token.getToken();
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
    public String getName() {return userID;}

    @Override
    public boolean isAuthenticated() {return authenticatedStatus;}

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {this.authenticatedStatus = b;}
}
