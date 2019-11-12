package edu.iastate.coms309.cyschedulebackend.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import javax.xml.stream.util.StreamReaderDelegate;
import java.util.Date;

public class JwtTokenUtils {
    public static void main(String[] args){
        System.out.println(generateToken("/api/v1/user/token","0b4817fa-2d65-4713-953c-7417f09bf6c9","e574871f-ed12-4354-9c45-5d5d47fcdf71",20000000));
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
