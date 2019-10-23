package edu.iastate.coms309.cyschedulebackend.Service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserTokenService {

    /*
       Maybe a improve point
           - User token will lost after server restart
           - Very Slow speed for creating jwt Token
           - May better if Permission object contains all related user


    */

    @Value("${account.security.token.authtoken.expiretime}")
    Integer authTokenExpireTime;

    @Value("${account.security.token.accesstoken.expiretime}")
    Integer accessTokenExpireTime;

    @Autowired AccountService accountService;

    @Autowired PermissionService permissionService;


    private HashMap<Long, HashMap<String,UserToken>> keyStorage;

    public UserTokenService(){
        keyStorage = new HashMap<>();
    }

    public UserToken load(String token){
        UserToken tokenObject = new UserToken();

        if (token == null)
            return null;

        DecodedJWT jwt = null;
        try {
            jwt = JWT.decode(token);
        } catch (JWTDecodeException exception){
            //Invalid token
        }

        tokenObject.setToken(token);
        tokenObject.setUserID(jwt.getClaim("userID").asLong());
        return tokenObject;
    }

    public UserToken creat(Long userID) {
        UserToken token = new UserToken();
        List<Integer> permissionList = new ArrayList<>();
        String password = accountService.getJwtKey(userID);

        if(!keyStorage.containsKey(userID)) {
            keyStorage.put(userID, new HashMap<>());
        }

        accountService.getPermissions(userID).forEach(V -> {
            permissionList.add(V.getRoleID());
        });

        token.setTokenID(UUID.randomUUID().toString());
        token.setRefreshKey(UUID.randomUUID().toString());
        Algorithm algorithmHS = Algorithm.HMAC256(password);

        token.setToken(JWT.create()
                .withIssuer("CySchedule")
                .withJWTId(token.getTokenID())
                .withClaim("userID",userID)
                .withExpiresAt(new Date(System.currentTimeMillis() + authTokenExpireTime))
                .withArrayClaim("permission", permissionList.toArray(new Integer[0]))
                .sign(algorithmHS));

        keyStorage.get(userID).put(token.getTokenID(),token);
        return token;
    }

    public boolean verify(UserToken tokenObject){
        String password = accountService.getJwtKey(tokenObject.getUserID());

        Algorithm algorithm = Algorithm.HMAC256(password);
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("CySchedule")
                    .acceptLeeway(1)   //1 sec for nbf and iat
                    .acceptExpiresAt(5)   //5 secs for exp
                    .build();
            DecodedJWT jwt = verifier.verify(tokenObject.getToken());
        } catch (JWTVerificationException exception) {
            return false;
        }
        return true;
    }
}
