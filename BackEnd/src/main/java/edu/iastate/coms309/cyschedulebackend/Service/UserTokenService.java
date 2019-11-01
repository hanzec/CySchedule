package edu.iastate.coms309.cyschedulebackend.Service;


import edu.iastate.coms309.cyschedulebackend.persistence.model.UserCredential;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserToken;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.UserLoginTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.*;

@Lazy
@Service
public class UserTokenService {

    /*
       Maybe a improve point
           - User token will lost after server restart
           - Very Slow speed for creating jwt Token
           - May better if Permission object contains all related user
           - Provide details for incorrect input using exception


    */

    @Value("${account.security.token.authtoken.expiretime}")
    Integer authTokenExpireTime;

    @Value("${account.security.token.accesstoken.expiretime}")
    Integer accessTokenExpireTime;

    @Autowired
    UserLoginTokenRepository userLoginTokenRepository;

    public UserToken creat(UserCredential userCredential) {
        UserToken token = new UserToken();

        token.setTokenID(UUID.randomUUID().toString());
        token.setSecret(UUID.randomUUID().toString());
        token.setRefreshKey(UUID.randomUUID().toString());
        token.setPermissions(userCredential.getPermissions());
        token.setExpireTime(new Time(System.currentTimeMillis() + authTokenExpireTime));

        token.setOwner(userCredential);
        this.update(token);
        return token;
    }

    public UserToken getTokenObject(String tokenID){return userLoginTokenRepository.getOne(tokenID);}

    public List<UserToken> getAllTokenBelongToUser(String email){return userLoginTokenRepository.getAllByUserEmail(email);}

    @Async
    @Transactional
    void update(UserToken userToken){userLoginTokenRepository.save(userToken);}
}
