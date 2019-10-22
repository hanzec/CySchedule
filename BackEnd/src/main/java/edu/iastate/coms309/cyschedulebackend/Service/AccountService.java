package edu.iastate.coms309.cyschedulebackend.Service;


import edu.iastate.coms309.cyschedulebackend.persistence.model.Permission;
import edu.iastate.coms309.cyschedulebackend.persistence.model.User;

import edu.iastate.coms309.cyschedulebackend.persistence.repository.UserDetailsRepository;
import edu.iastate.coms309.cyschedulebackend.persistence.requestModel.LoginRequest;
import edu.iastate.coms309.cyschedulebackend.security.model.LoginObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import java.util.Set;
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
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    private HashMap<Long,String> challengeStorage = new HashMap<>();

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Transactional
    public User createUser(LoginRequest user) {

        User submit = new User();

        submit.setEmail(user.getEmail());
        submit.setPassword(user.getPassword());
        submit.setUsername(user.getUsername());
        submit.setLastName(user.getLastName());
        submit.setFirstName(user.getFirstName());

        //encrypt password
        submit.setRegisterTime(System.currentTimeMillis()/1000L);
        submit.setPassword(passwordEncoder.encode(user.getPassword()));

        //save to the
        userDetailsRepository.save(submit);

        logger.info("New User with id :[" + submit.getUserID() + "] is created");
        return submit;
    }

    @Transactional
    public boolean existsByEmail(String email){ return userDetailsRepository.existsByEmail(email);}

    @Transactional
    @Cacheable(value = "userid", key = "#email + '_id'")
    public Long getUserID(String email) { return userDetailsRepository.findByEmail(email).getUserID(); }

    @Transactional
    @Cacheable(value = "userEmail", key = "#userID + '_email'")
    public String getUserEmail(Long userID) { return userDetailsRepository.findByUserID(userID).getEmail(); }

    @Transactional
    @Cacheable(value = "salt", key = "#email + '_salt'")
    public String getUserSalt(String email) {
        String result = userDetailsRepository.findByEmail(email).getPassword();
        return result.split("[.]")[1];
    }

    @Transactional
    public Set<Permission> getPermissions(Long userID){
        return userDetailsRepository.findByUserID(userID).getPermissions();
    }
    @Transactional
    @Cacheable(value = "jwt_key", key = "#userID + '_jwt_key'")
    public String getJwtKey(Long userID) {
        User user = userDetailsRepository.findByUserID(userID);

        if(user.getJwtKey() == null){
            user.setJwtKey(UUID.randomUUID().toString());
            logger.info("New jwt Keys for [" + userID + "] is created");
            userDetailsRepository.save(user);
        }

        return user.getJwtKey();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (userDetailsRepository.existsByEmail(email))
            return new LoginObject(userDetailsRepository.findByEmail(email));
        else
            throw new UsernameNotFoundException("username " + email + " is not found");
    }

    public boolean checkPassword(String email, String password){
        return passwordEncoder.matches(password, userDetailsRepository.findByEmail(email).getPassword());
    }

    public String createChallengeKeys(Long userID){
        logger.info("New Challenge Keys for [" + userID + "] is created");
        challengeStorage.put(userID, UUID.randomUUID().toString());
        return challengeStorage.get(userID);
    }

    public String getChallengeKeys(Long userID){
        if(challengeStorage.containsKey(userID))
            return challengeStorage.get(userID);
        else
            return createChallengeKeys(userID);
    }
}
