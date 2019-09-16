package edu.iastate.coms309.cyschedulebackend.persistence.dao;

import com.google.gson.Gson;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserToken;

import javax.persistence.criteria.CriteriaBuilder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class TokenDAOImpl implements TokenDAO {
     /*
        Maybe a improve point
            - Token should be cased in redis for better performance

     */

    @Autowired SessionFactory sessionFactory;

    //只在这里缓存请求
    UserDAO userDAO = new UserDAOImpl();

    @Value("${account.security.tokenLength}")
    Integer tokenLength;

    @Value("${account.security.token.authtoken.expiretime}")
    Integer authTokenExpireTime;

    @Value("${account.security.token.accesstoken.expiretime}")
    Integer accessTokenExpireTime;

    @Override
    public UserToken refreshToken(String token, String userID) {
        User user = userDAO.findByUserID(userID);
        UserToken userToken = getToken(token,userID);

        user.getUserToken().remove(userToken);

        Session session = sessionFactory.openSession();

        session.beginTransaction();

        session.remove(userToken);

        session.update(user);

        session.getTransaction().commit();

        session.close();

        return generateToken()
    }

    @Override
    public void grantTokenPermission(String userID, String token, TokenRight tokenRight) {
        UserToken userToken = getToken(userID,token);

        switch (tokenRight){
            case Refresh:
                userToken.setAllowRefresh();
                break;
            case AccessAPIs:
                userToken.setAllowAccessAPIs();
                break;
            case Authentication:
                userToken.setAllowAuthentication();
                break;
        }

        Session session = sessionFactory.openSession();

        session.beginTransaction();

        session.update(userToken);

        session.getTransaction().commit();

        session.close();
    }

    @Override
    public UserToken getToken(String token,String userID) {
        User user = userDAO.findByUserID(userID);

        for (UserToken userToken :user.getUserToken()) {
            if (userToken.getToken().equals(token))
                return userToken;
        }

        return null;
    }



    @Override
    public UserToken generateToken(String userID) {
        UserToken userToken = new UserToken();
        User user = userDAO.findByUserID(userID);

        userToken.setOwner(user);
        userToken.setToken(this.generateToken(tokenLength));
        userToken.setRefreshPassword(this.generateToken(tokenLength));

        Session session = sessionFactory.openSession();

        session.beginTransaction();

        session.save(userToken);

        session.update(user);

        session.getTransaction().commit();

        session.close();

        return userToken;
    }

    @Override
    public boolean isExpired(String token, String userID) {
        return this.getToken(token,userID).getExpireTime() > System.currentTimeMillis()/1000L;
    }

    private String generateToken(Integer tokenLength){
        SecureRandom random = null;
        byte[] randomTmp = new byte[tokenLength];

        try {
            //User SecureRandom to avoid thread problem
            random = SecureRandom.getInstance("SHA1PRNG");
            random.nextBytes(randomTmp);
            return Arrays.toString(randomTmp);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
