package edu.iastate.coms309.cyschedulebackend.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import javax.xml.stream.util.StreamReaderDelegate;
import java.util.Date;

public class JwtTokenUtils {
    public static void main(String[] args){
        System.out.println(generateToken("/api/v1/user/token","4312fea6-bd2f-4c98-be47-a2aa51765f6c","8b7b6393-d264-4396-ab0f-2bcfe29b8292",20000000));
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
