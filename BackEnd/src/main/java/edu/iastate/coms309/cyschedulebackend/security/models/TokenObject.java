package edu.iastate.coms309.cyschedulebackend.security.models;

import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Permission;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class TokenObject implements UserDetails {

    private Long userID ;

    private String token ;

    private Collection<? extends GrantedAuthority> userPermission;

    @Override
    public boolean isEnabled() { return true; }

    @Override
    public String getPassword() { return token; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public String getUsername() { return userID.toString(); }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return userPermission; }
}
