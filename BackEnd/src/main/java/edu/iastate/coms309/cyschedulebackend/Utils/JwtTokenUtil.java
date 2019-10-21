package edu.iastate.coms309.cyschedulebackend.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtTokenUtil {

    public LoginToken generateNewToken(Long userID, Integer validTimes, String password){
        String keyID = UUID.randomUUID().toString();
        String refreshKey = UUID.randomUUID().toString();
        Algorithm algorithmHS = Algorithm.HMAC256(password);

        String token = JWT.create()
                .withJWTId(keyID)
                .withIssuer("CySchedule")
                .withClaim("userID",userID)
                .withExpiresAt(new Date(System.currentTimeMillis() + validTimes))
                .sign(algorithmHS);

        return new LoginToken(token,refreshKey,userID);
    }

    public static LoginToken JwtFromString(String token){
        DecodedJWT jwt = null;
        try {
            jwt = JWT.decode(token);
        } catch (JWTDecodeException exception){
            //Invalid token
        }
        assert jwt != null;
        return new LoginToken(token,jwt.getHeaderClaim("userID").asLong());
    }

    public static boolean isTokenValid(LoginToken loginToken, String password){
        Algorithm algorithm = Algorithm.HMAC256(password);
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("CySchedule")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(loginToken.getToken());
        } catch (JWTVerificationException exception) {
            return false;
        }
        return true;
    }

    public static boolean isTokenExpired(LoginToken loginToken, String password){
        Algorithm algorithm = Algorithm.HMAC256(password);
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .acceptLeeway(1)   //1 sec for nbf and iat
                    .acceptExpiresAt(5)   //5 secs for exp
                    .build();
            DecodedJWT jwt = verifier.verify(loginToken.getToken());
        } catch (JWTVerificationException exception) {
            return false;
        }
        return true;
    }
}
