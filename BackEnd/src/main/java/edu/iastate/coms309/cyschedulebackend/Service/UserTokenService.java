package edu.iastate.coms309.cyschedulebackend.Service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserCredential;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserLoginToken;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.UserCredentialRepository;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.UserLoginTokenRepository;
import edu.iastate.coms309.cyschedulebackend.security.model.TokenObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class UserTokenService {

    /*
       Maybe a improve point
           - User token will lost after server restart
           - Very Slow speed for creating jwt Token
           - May better if Permission object contains all related user
           - Provide details for incorrect input using exception


    */

    @Value("${account.security.token.authtoken.expiretime}")
    Integer authTokenExpireTime;

    @Value("${account.security.token.accesstoken.expiretime}")
    Integer accessTokenExpireTime;

    @Autowired
    UserLoginTokenRepository userLoginTokenRepository;

    public TokenObject load(String token) throws NullPointerException,JWTDecodeException{
        TokenObject tokenObject = new TokenObject();

        if (token == null)
            throw new NullPointerException("Token is empty");

        DecodedJWT jwt = JWT.decode(token);

        tokenObject.setToken(token);
        tokenObject.setUserID(jwt.getClaim("userID").asString());
        tokenObject.setTokenID(jwt.getClaim("tokenID").asString());
        return tokenObject;
    }

    public UserLoginToken creat(UserCredential userCredential) {
        UserLoginToken token = new UserLoginToken();

        token.setTokenID(UUID.randomUUID().toString());
        token.setRefreshKey(UUID.randomUUID().toString());

        Algorithm algorithmHS = Algorithm.HMAC256(userCredential.getJwtKey());

        token.setToken(JWT.create()
                .withIssuer("CySchedule")
                .withJWTId(token.getTokenID())
                .withClaim("userID",userCredential.getUserID())
                .withExpiresAt(new Date(System.currentTimeMillis() + authTokenExpireTime))
                .sign(algorithmHS));

        token.setOwner(userCredential);
        this.update(token);
        return token;
    }

    @Async
    @Transactional
    void update(UserLoginToken userLoginToken){userLoginTokenRepository.save(userLoginToken);}

    public boolean verify(TokenObject tokenObject,UserCredential userCredential){
        String password = userCredential.getJwtKey();

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
