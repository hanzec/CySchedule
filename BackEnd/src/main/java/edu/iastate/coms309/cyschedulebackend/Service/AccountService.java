package edu.iastate.coms309.cyschedulebackend.Service;


import edu.iastate.coms309.cyschedulebackend.persistence.model.User;

import edu.iastate.coms309.cyschedulebackend.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
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
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    HashMap<String,byte[]> challengeStorage = new HashMap<>();

    @Transactional
    public Long createUser(String password, String firstName, String lastName, String email, String username) {
        // create new user object
        User user = new User(password,firstName,lastName,email,username);

        //encrypt password
        user.setPassword(passwordEncoder.encode(password));

        //save to the
        userRepository.save(user);

        return user.getUserID();
    }

    @Transactional
    public boolean existsByEmail(String email){ return userRepository.existsByEmail(email);}

    @Transactional
    @Cacheable(value = "userid", key = "'id_'+#email")
    public Long getUserID(String email) { return userRepository.findByEmail(email).getUserID(); }

    @Transactional
    @Cacheable(value = "salt", key = "'salt_'+#email")
    public String getUserSalt(String email) { return userRepository.findByEmail(email).getPassword().split(".")[2]; }

    @Transactional
    @Cacheable(value = "jwt_key", key = "'jwt_key_'+#userID")
    public String getJwtKey(Long userID) { return userRepository.findByUserID(userID).getJwtKey(); }

    @Transactional
    @Cacheable(value = "password", key = "'user_pass_'+#email")
    public String getPasswordByEmail(String email) { return userRepository.findByEmail(email).getPassword(); }

    public byte[] getChallengeKeys(String username){
        if (challengeStorage.containsKey(username))
            return challengeStorage.get(username);
        else
            return generateChallengeKeys(username);
    }

    public byte[] generateChallengeKeys(String username){
        challengeStorage.put(username, UUID.randomUUID().toString().getBytes());
        return challengeStorage.get(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (userRepository.existsByEmail(s))
            return userRepository.findByEmail(s);
        else
            throw new UsernameNotFoundException("username " + s + " is not found");
    }
}
