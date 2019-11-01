package edu.iastate.coms309.cyschedulebackend.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import javax.xml.stream.util.StreamReaderDelegate;
import java.util.Date;

public class JwtTokenUtils {
    public static void main(String[] args){
        System.out.println(generateToken("/api/v1/user/token","d99513ca-feb6-4e16-a77e-396d7de60405","1e84f975-2ddb-4a94-8ae4-a9cb95bdbad0",20000000));
    }

    static String generateToken(String requestUrl, String tokenID, String password, Integer expireTime){
        Algorithm algorithmHS = Algorithm.HMAC256(password);

        return JWT.create()
                .withIssuer("CySchedule")
                .withJWTId(tokenID)
                .withClaim("requestUrl",requestUrl)
                .withExpiresAt(new Date(System.currentTimeMillis() + expireTime))
                .sign(algorithmHS);
    }
}
