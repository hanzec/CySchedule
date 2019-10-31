package edu.iastate.coms309.cyschedulebackend.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import javax.xml.stream.util.StreamReaderDelegate;
import java.util.Date;

public class JwtTokenUtils {
    public static void main(String[] args){
        System.out.println(generateToken("/api/v1/event/add","aca95182-66ee-460e-a329-5e3a1e9e24e2","3e63e4d8-bfe7-4dbd-af9b-8a834d8a0a8d",20000000));
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
