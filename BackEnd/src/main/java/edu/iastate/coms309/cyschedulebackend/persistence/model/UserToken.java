package edu.iastate.coms309.cyschedulebackend.persistence.model;

import lombok.Data;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.shiro.authc.AuthenticationToken;

@Data
public class UserToken{

    private String token;

    private String refreshKey;

    private String tokenSecret;


    public UserToken(String token, String tokenSecret, String refreshKey){
        this.token = token;
        this.refreshKey = refreshKey;
        this.tokenSecret = tokenSecret;
    }
}
