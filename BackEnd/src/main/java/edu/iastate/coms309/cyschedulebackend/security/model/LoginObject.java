package edu.iastate.coms309.cyschedulebackend.security.model;

import edu.iastate.coms309.cyschedulebackend.persistence.model.Permission;
import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class LoginObject implements UserDetails {

    private Long userID;

    private String password;

    private Collection<Permission> userPermission;

    public LoginObject(User user){
        this.userID = user.getUserID();
        this.password = user.getPassword();
        this.userPermission = user.getPermissions();
    }

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
