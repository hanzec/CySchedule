package edu.iastate.coms309.cyschedulebackend.Account;

import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SecurityObject implements org.springframework.security.core.userdetails.UserDetails {

    String userID;
    String enabled;
    String password;

    Map<String, UserToken> userTokenMap;

    public SecurityObject(User user){
        this.userID = user.getUserID();
        this.password = user.getPassword();
        for (UserToken userToken : user.getUserToken()){
            userTo

        }
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
