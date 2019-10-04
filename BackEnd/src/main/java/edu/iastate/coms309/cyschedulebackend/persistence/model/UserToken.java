package edu.iastate.coms309.cyschedulebackend.persistence.model;

import jdk.nashorn.internal.objects.NativeUint8Array;
import lombok.Data;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.shiro.authc.AuthenticationToken;

@Data
public class UserToken implements AuthenticationToken{

    private String token;

    private String userID;

    private String refreshKey;

    public UserToken(String token,String userID){
        this.token = token;
        this.userID = userID;
        this.refreshKey = null;;
    }

    public UserToken(String token,String refreshKey,String userID){
        this.token = token;
        this.userID = userID;
        this.refreshKey = refreshKey;
    }

    @Override
    public Object getPrincipal() { return userID;}

    @Override
    public Object getCredentials() { return token;}
}
