package edu.iastate.coms309.cyschedulebackend.Service;


import edu.iastate.coms309.cyschedulebackend.Utils.JwtTokenUtil;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.UserTokenDAO;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserToken;
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

    public boolean verify(UserToken userToken) {
        UserToken cached = (UserToken) redisTemplate.opsForHash()
                .get("user_token_" + userToken.getUserID(), userToken.getToken());

        if (cached == null) {
            return false;
        } else if (!JwtTokenUtil.isTokenExpired(userToken, accountService.getJwtKey(userToken.getUserID()))) {
            return false;
        } else if (!JwtTokenUtil.isTokenValid(userToken, accountService.getJwtKey(userToken.getUserID()))){
            return false;
        }else
            return true;
    }

    public UserToken genUserToken(String userID) {

        UserToken userToken = jwtTokenUtil.generateNewToken(userID,
                                                            authTokenExpireTime,
                                                            accountService.getJwtKey(userID));

        //CacheItem
        redisTemplate.opsForHash().put("user_token_" + userID,userToken.getToken(),userToken);

        return userToken;
    }

    public void deleteUserToken(String userID, String token) {
        redisTemplate.opsForHash().delete("user_token_" + userID,token);
    }

    public UserToken getToken(String userID, String token){
        return (UserToken) redisTemplate.opsForHash().get("user_token_" + userID,token);
    }
}
