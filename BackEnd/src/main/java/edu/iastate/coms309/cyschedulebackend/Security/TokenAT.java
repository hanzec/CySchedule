package edu.iastate.coms309.cyschedulebackend.Security;

import org.apache.shiro.authc.AuthenticationToken;

public class TokenAT implements AuthenticationToken {

    private String token;

    private String userID;

    public TokenAT(String token, String userID){
        this.token = token;
        this.userID = userID;
    }

    @Override
    public Object getPrincipal() {return  userID;}

    @Override
    public Object getCredentials() {return userID; }
}
