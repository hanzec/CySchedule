package edu.iastate.coms309.cyschedulebackend.Service;


import edu.iastate.coms309.cyschedulebackend.Utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserTokenService {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    AccountService accountService;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Value("${account.security.token.authtoken.expiretime}")
    Integer authTokenExpireTime;

    @Value("${account.security.token.accesstoken.expiretime}")
    Integer accessTokenExpireTime;

    public boolean verify(LoginToken loginToken) {
        LoginToken cached = (LoginToken) redisTemplate.opsForHash()
                .get("user_token_" + loginToken.getUserID(), loginToken.getToken());

        if (cached == null)
            return false;
        else if (!JwtTokenUtil.isTokenExpired(loginToken,accountService.getJwtKey(loginToken.getUserID())))
            return false;
        else if (!JwtTokenUtil.isTokenValid(loginToken, accountService.getJwtKey(loginToken.getUserID())))
            return false;
        else
            return true;
    }

    public LoginToken genUserToken(Long userID) {

        LoginToken loginToken = jwtTokenUtil.generateNewToken(userID,
                                                            authTokenExpireTime,
                                                            accountService.getJwtKey(userID));

        //CacheItem
        redisTemplate.opsForHash().put("user_token_" + userID, loginToken.getToken(), loginToken);

        return loginToken;
    }

    public void deleteUserToken(Long userID, String token) {
        redisTemplate.opsForHash().delete("user_token_" + userID,token);
    }

    public LoginToken getToken(Long userID, String token){
        return (LoginToken) redisTemplate.opsForHash().get("user_token_" + userID,token);
    }
}
