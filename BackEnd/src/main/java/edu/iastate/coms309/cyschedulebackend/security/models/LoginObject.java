package edu.iastate.coms309.cyschedulebackend.security.models;

import edu.iastate.coms309.cyschedulebackend.persistence.model.Permission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class LoginObject implements UserDetails {

    private Long userID;

    private String password;

    private Collection<Permission> userPermission;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return userPermission; }

    @Override
    public String getPassword() { return userID + "." + password; }

    @Override
    public String getUsername() { return userID.toString(); }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
