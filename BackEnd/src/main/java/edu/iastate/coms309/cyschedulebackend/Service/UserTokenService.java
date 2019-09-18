package edu.iastate.coms309.cyschedulebackend.Service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import edu.iastate.coms309.cyschedulebackend.Utils.PasswordUtil;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.UserTokenDAO;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserToken;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserTokenService implements UserTokenDAO {

    @Autowired
    PasswordUtil passwordUtil;

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Value("${account.security.token.authtoken.expiretime}")
    Integer authTokenExpireTime;

    @Value("${account.security.token.accesstoken.expiretime}")
    Integer accessTokenExpireTime;

    @Override
    public String genUserToken(String userID) {
        String keyID = passwordUtil.generateSalt();
        String secret = passwordUtil.generateSalt();
        String refreshKey = passwordUtil.generateSalt();
        Algorithm algorithmHS = Algorithm.HMAC256(secret);

        String token = JWT.create()
                .withJWTId(keyID)
                .withIssuer("CySchedule")
                .withClaim("userID",userID)
                .withClaim("tokenID",redisTemplate.opsForList().size(userID))
                .withExpiresAt(new Date(System.currentTimeMillis() + authTokenExpireTime))
                .sign(algorithmHS);

        UserToken userToken = new UserToken(token,secret,refreshKey);

        //CacheItem
        redisTemplate.opsForHash().put("user_token_" + userID,token,userToken );

        return token;
    }

    @Override
    public void deleteUserToken(String userID, String token) {
        redisTemplate.opsForHash().delete(userID,token);
    }

    public UserToken getToken(String userID, String token){
        return (UserToken) redisTemplate.opsForHash().get("user_token_" + userID,token);
    }

    public boolean isTokenValid(String userID, String token){
        UserToken userToken = (UserToken) redisTemplate.opsForHash().get("user_token_" + userID,token);

        Algorithm algorithm = Algorithm.HMAC256(userToken.getTokenSecret());
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
        } catch (JWTVerificationException exception) {
            return false;
        }
        return true;
    }

    public boolean isTokenExpired(String userID, String token){
        UserToken userToken = (UserToken) redisTemplate.opsForHash().get("user_token_" + userID,token);

        Algorithm algorithm = Algorithm.HMAC256(userToken.getTokenSecret());
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .acceptLeeway(1)   //1 sec for nbf and iat
                    .acceptExpiresAt(5)   //5 secs for exp
                    .build();
            DecodedJWT jwt = verifier.verify(token);
        } catch (JWTVerificationException exception) {
            return false;
        }
        return true;
    }
}
