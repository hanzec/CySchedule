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
import org.springframework.stereotype.Service;

import java.util.*;

@Service
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
    AccountService accountService;

    @Autowired
    UserLoginTokenRepository userLoginTokenRepository;


    private HashMap<String, HashMap<String, UserLoginToken>> keyStorage;

    public UserTokenService(){
        keyStorage = new HashMap<>();
    }

    public TokenObject load(String token){
        TokenObject tokenObject = new TokenObject();

        if (token == null)
            return null;

        DecodedJWT jwt = null;
        try {
            jwt = JWT.decode(token);
        } catch (JWTDecodeException exception){
            return null; //should give an exception not null NEED Fix
        }

        tokenObject.setToken(token);
        tokenObject.setUserID(jwt.getClaim("userID").asString());
        tokenObject.setTokenID(jwt.getClaim("tokenID").asString());
        return tokenObject;
    }

    public UserLoginToken creat(String email) {
        UserLoginToken token = new UserLoginToken();
        UserCredential userCredential = (UserCredential) accountService.loadUserByUsername(email);

        token.setTokenID(UUID.randomUUID().toString());
        token.setRefreshKey(UUID.randomUUID().toString());

        if(!keyStorage.containsKey(userCredential.getUserID())) {
            keyStorage.put(userCredential.getUserID(), new HashMap<>());
        }

        Algorithm algorithmHS = Algorithm.HMAC256(accountService.getJwtKey(userCredential.getEmail()));

        token.setToken(JWT.create()
                .withIssuer("CySchedule")
                .withJWTId(token.getTokenID())
                .withClaim("userID",userCredential.getUserID())
                .withExpiresAt(new Date(System.currentTimeMillis() + authTokenExpireTime))
                .sign(algorithmHS));


        token.setOwner(userCredential);
        userLoginTokenRepository.save(token);
        keyStorage.get(userCredential.getUserID()).put(token.getTokenID(),token);


        return token;
    }

    public boolean verify(TokenObject tokenObject){
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
