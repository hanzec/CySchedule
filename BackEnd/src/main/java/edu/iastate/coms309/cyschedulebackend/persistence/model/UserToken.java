package edu.iastate.coms309.cyschedulebackend.persistence.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class UserToken {

    String token;

    String refreshKey;

    List<GrantedAuthority> permissions;

}
