package edu.iastate.coms309.cyschedulebackend.Service;


import edu.iastate.coms309.cyschedulebackend.Utils.PasswordUtil;

import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserRole;

import edu.iastate.coms309.cyschedulebackend.persistence.repository.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.AbstractCachingConfiguration;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.UUID;

@Service
public class AccountService implements UserDetailsService {
    /*
        Maybe a improve point
            - Session could reuse for whole class (2019-9-13)
                * Problem may happened when this instance is closed without close session
            - After some operation my gen waste @ redis see updateEmail
            - Cache may not accurate after delete user (2019-9-16)
            - exception

     */
    @Autowired
    PasswordUtil passwordUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Transactional
    public Long createUser(String password, String firstName, String lastName, String email, String username) {

        // create new user object
        User user = new User(password,firstName,lastName,email,username);

        //encrypt password
        user.setSalt(passwordUtil.generateSalt());
        user.setPassword(passwordUtil.generatePasswordPBKDF2(user.getPassword(),user.getSalt()));

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
    public String getUserSalt(String email) { return userRepository.findByEmail(email).getSalt(); }

    @Transactional
    @Cacheable(value = "jwt_key", key = "'jwt_key_'+#userID")
    public String getJwtKey(Long userID) { return userRepository.findByUserID(userID).getJwtKey(); }

    @Transactional
    @Cacheable(value = "password", key = "'user_pass_'+#email")
    public String getPasswordByEmail(String email) { return userRepository.findByEmail(email).getPassword(); }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (userRepository.existsByEmail(s))
            return userRepository.findByEmail(s);
        else
            throw new UsernameNotFoundException("username " + s + " is not found");
    }
}
