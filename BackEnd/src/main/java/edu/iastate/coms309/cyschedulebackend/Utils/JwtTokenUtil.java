package edu.iastate.coms309.cyschedulebackend.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.sun.corba.se.impl.oa.toa.TOA;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JwtTokenUtil {

    @Autowired
    PasswordUtil passwordUtil;

    public UserToken generateNewToken(String userID, Integer validTimes, String password){
        String keyID = passwordUtil.generateSalt();
        String refreshKey = passwordUtil.generateSalt();
        Algorithm algorithmHS = Algorithm.HMAC256(password);

        String token = JWT.create()
                .withJWTId(keyID)
                .withIssuer("CySchedule")
                .withClaim("userID",userID)
                .withExpiresAt(new Date(System.currentTimeMillis() + validTimes))
                .sign(algorithmHS);

        return new UserToken(token,refreshKey,userID);
    }

    public static UserToken JwtFromString(String token){
        DecodedJWT jwt = null;
        try {
            jwt = JWT.decode(token);
        } catch (JWTDecodeException exception){
            //Invalid token
        }
        assert jwt != null;
        return new UserToken(token,jwt.getHeaderClaim("userID").asString());
    }

    public static boolean isTokenValid(UserToken userToken, String password){
        Algorithm algorithm = Algorithm.HMAC256(password);
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("CySchedule")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(userToken.getToken());
        } catch (JWTVerificationException exception) {
            return false;
        }
        return true;
    }

    public static boolean isTokenExpired(UserToken userToken,String password){
        Algorithm algorithm = Algorithm.HMAC256(password);
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .acceptLeeway(1)   //1 sec for nbf and iat
                    .acceptExpiresAt(5)   //5 secs for exp
                    .build();
            DecodedJWT jwt = verifier.verify(userToken.getToken());
        } catch (JWTVerificationException exception) {
            return false;
        }
        return true;
    }
}
