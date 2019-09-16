package edu.iastate.coms309.cyschedulebackend.account;

import org.springframework.security.core.GrantedAuthority;

public class SecurityRole implements GrantedAuthority {

    @Override
    public String getAuthority() {
        return null;
    }
}
