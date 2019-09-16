package edu.iastate.coms309.cyschedulebackend.persistence.dao;

import com.google.gson.Gson;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserToken;

import java.util.concurrent.TimeUnit;

public class TokenDAOImpl implements TokenDAO {

    //只在这里缓存请求

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${account.security.tokenLength}")
    Integer tokenLength;

    @Override
    public UserToken getToken(String Token) {
        return null;
    }

    @Override
    public UserToken refreshToken(String token) {
        return null;
    }

    @Override
    public UserToken generateAuthToken(String userID) {

    }

    @Override
    public UserToken generateAccessToken(String userID, TokenType type) {
        return null;
    }

    @Override
    public boolean isValid(String token, String userID) {
        return false;
    }

    private void addToCache(String key, long time, User user) {
        Gson gson = new Gson();
        redisTemplate.opsForValue().set(key, gson.toJson(user), time, TimeUnit.MINUTES);
    }

    private void
    public void deleteFromCache(String key) { redisTemplate.opsForValue().getOperations().delete(key); }
}
