package edu.iastate.coms309.cyschedulebackend.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import javax.xml.stream.util.StreamReaderDelegate;
import java.util.Date;

public class JwtTokenUtils {
    public static void main(String[] args){
        System.out.println(generateToken("/api/v1/user/token","0e78b69d-4654-4789-bc5c-034bbded66e4","44e29fab-80d3-4980-a861-a129b647568b",20000000));
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
