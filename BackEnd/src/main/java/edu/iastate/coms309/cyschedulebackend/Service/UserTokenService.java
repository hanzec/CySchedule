package edu.iastate.coms309.cyschedulebackend.Service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import edu.iastate.coms309.cyschedulebackend.Utils.JwtTokenUtil;
import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserToken;
import edu.iastate.coms309.cyschedulebackend.security.models.LoginObject;
import edu.iastate.coms309.cyschedulebackend.security.models.TokenObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class UserTokenService {

    @Value("${account.security.token.authtoken.expiretime}")
    Integer authTokenExpireTime;

    @Value("${account.security.token.accesstoken.expiretime}")
    Integer accessTokenExpireTime;

    @Autowired
    AccountService accountService;


    HashMap<Long, List<UserToken>> keyStorage;

    @Cacheable(value = "tokenObject", key = "#token + '_object")
    public TokenObject load(String token){
        TokenObject tokenObject = new TokenObject();

        DecodedJWT jwt = null;
        try {
            jwt = JWT.decode(token);
        } catch (JWTDecodeException exception){
            //Invalid token
        }

        tokenObject.setToken(token);
        tokenObject.setUserID(jwt.getClaim("userID").asLong());
        for (String permission : jwt.getClaim("Permission").toString().split("[|]")) {
            tokenObject.getAuthorities().add()
        }
    }

    public UserToken creat(LoginObject user) {
    }
}
