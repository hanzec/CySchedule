package edu.iastate.coms309.cyschedulebackend.Service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import edu.iastate.coms309.cyschedulebackend.Utils.PasswordUtil;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Autowired
    PasswordUtil passwordUtil;

    @Autowired
    SessionFactory sessionFactory;

    @Value("${account.security.token.authtoken.expiretime}")
    Integer authTokenExpireTime;

    @Value("${account.security.token.accesstoken.expiretime}")
    Integer accessTokenExpireTime;



    public String generateAuthToken(String userid){
        String keyID = passwordUtil.generateSalt();
        String secret = passwordUtil.generateSalt();
        String refreshKey = passwordUtil.generateSalt();
        Algorithm algorithmHS = Algorithm.HMAC256(secret);


        String token = JWT.create()
                .withJWTId(keyID)
                .withIssuer("CySchedule")
                .withClaim("userID",userid)
                .withClaim("refreshID",refreshKey)
                .withExpiresAt(new Date(System.currentTimeMillis() + authTokenExpireTime))
                .sign(algorithmHS);
    }

}
