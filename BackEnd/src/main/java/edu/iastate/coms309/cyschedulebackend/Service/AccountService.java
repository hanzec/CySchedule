package edu.iastate.coms309.cyschedulebackend.Service;


import edu.iastate.coms309.cyschedulebackend.persistence.model.User;

import edu.iastate.coms309.cyschedulebackend.persistence.repository.UserDetailsRepository;
import edu.iastate.coms309.cyschedulebackend.security.PBKDF2PasswordEncoder;
import edu.iastate.coms309.cyschedulebackend.security.models.LoginObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.UUID;

@Service
public class AccountService implements UserDetailsService{
    /*
        Maybe a improve point
            - Session could reuse for whole class (2019-9-13)(FIXED)
                * Problem may happened when this instance is closed without close session
            - After some operation my gen waste @ redis see updateEmail
            - Cache may not accurate after delete user (2019-9-16)
            - exception

     */

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    PBKDF2PasswordEncoder passwordEncoder;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    private HashMap<Long,String> challengeStorage = new HashMap<>();

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Transactional
    public User createUser(User user) {

        //encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //save to the
        userDetailsRepository.save(user);

        logger.info("New User with id :[" + user.getUserID() + "] is created");
        return user;
    }

    @Transactional
    public boolean existsByEmail(String email){ return userDetailsRepository.existsByEmail(email);}

    @Transactional
    @Cacheable(value = "userid", key = "'id_'+#email")
    public Long getUserID(String email) { return userDetailsRepository.findByEmail(email).getUserID(); }

    @Transactional
    @Cacheable(value = "salt", key = "'salt_'+#email")
    public String getUserSalt(String email) {
        String result = userDetailsRepository.findByEmail(email).getPassword();
        return result.split("[.]")[1];
    }

    @Transactional
    @Cacheable(value = "jwt_key", key = "'jwt_key_'+#userID")
    public String getJwtKey(Long userID) { return userDetailsRepository.findByUserID(userID).getJwtKey(); }

    @Transactional
    @Cacheable(value = "password", key = "'user_pass_'+#email")
    public String getPasswordByEmail(String email) { return userDetailsRepository.findByEmail(email).getPassword(); }

    public byte[] getChallengeKeys(Long userID){
        if (challengeStorage.containsKey(userID)) {
            logger.info("New Challenge Keys for [" + userID + "] is created");
            return challengeStorage.get(userID).getBytes();
        }else
            return generateChallengeKeys(userID);
    }

    private byte[] generateChallengeKeys(Long userID){
        challengeStorage.put(userID, UUID.randomUUID().toString());
        return challengeStorage.get(userID).getBytes();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (userDetailsRepository.existsByEmail(email))
            return new LoginObject(userDetailsRepository.findByEmail(email));
        else
            throw new UsernameNotFoundException("username " + email + " is not found");
    }
}
