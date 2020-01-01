package edu.iastate.coms309.cyschedulebackend.Service;


import com.google.gson.Gson;
import edu.iastate.coms309.cyschedulebackend.exception.auth.token.TokenNotFoundException;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserCredential;
import edu.iastate.coms309.cyschedulebackend.persistence.model.permission.Permission;
import edu.iastate.coms309.cyschedulebackend.persistence.model.permission.UserToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserTokenService {

    /*
       Maybe a improve point
           - User token will lost after server restart
           - Provide details for incorrect input using exception
    */

    private final Gson gson = new Gson();
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${account.security.token.authtoken.expiretime}")
    Integer tokenExpireTime;

    @Value("${account.security.token.accesstoken.expiretime}")
    Integer refreshTokenExpireTime;

    public UserTokenService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public UserToken newToken(Set<Permission> permissions,
                              UserToken.TokenType tokenType,
                              UserCredential userCredential){

        UserToken newToken = new UserToken();
        ValueOperations<String,String> ops = redisTemplate.opsForValue();

        newToken.setTokenType(tokenType);
        newToken.setPermissions(permissions);
        newToken.setUserID(userCredential.getUserID());
        newToken.setSecret(UUID.randomUUID().toString());
        newToken.setTokenID(UUID.randomUUID().toString());

        ops.set("USER_TOKEN::" + newToken.getTokenID(),
                gson.toJson(newToken),
                tokenType == UserToken.TokenType.access_token ? tokenExpireTime : refreshTokenExpireTime,
                TimeUnit.SECONDS);
        return newToken;
    }

    public UserToken getToken(String tokenID) throws TokenNotFoundException{
        String key = "USER_TOKEN::" + tokenID;
        ValueOperations<String,String> ops = redisTemplate.opsForValue();

        if(redisTemplate.hasKey(key))
            throw new TokenNotFoundException();

        return gson.fromJson(ops.get(key),UserToken.class);
    }

    public void revokeToken(String tokenID) throws TokenNotFoundException{
        String key = "USER_TOKEN::" + tokenID;

        if(redisTemplate.hasKey(key))
            throw new TokenNotFoundException();
        else
            redisTemplate.delete(key);
    }


}
