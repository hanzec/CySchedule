package edu.iastate.coms309.cyschedulebackend.Service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.sun.codemodel.internal.JCase;
import edu.iastate.coms309.cyschedulebackend.Utils.JwtTokenUtil;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.UserTokenDAO;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserToken;
import org.apache.shiro.session.mgt.DelegatingSession;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserTokenService implements UserTokenDAO {

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

    @Override
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

    @Override
    public UserToken genUserToken(String userID) {

        UserToken userToken = jwtTokenUtil.generateNewToken(userID,
                                                            authTokenExpireTime,
                                                            accountService.getJwtKey(userID));

        //CacheItem
        redisTemplate.opsForHash().put("user_token_" + userID,userToken.getToken(),userToken);

        return userToken;
    }

    @Override
    public void deleteUserToken(String userID, String token) {
        redisTemplate.opsForHash().delete("user_token_" + userID,token);
    }

    public UserToken getToken(String userID, String token){
        return (UserToken) redisTemplate.opsForHash().get("user_token_" + userID,token);
    }
}
