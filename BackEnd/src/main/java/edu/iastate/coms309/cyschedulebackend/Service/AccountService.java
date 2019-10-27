package edu.iastate.coms309.cyschedulebackend.Service;


import edu.iastate.coms309.cyschedulebackend.persistence.model.Permission;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserCredential;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserInformation;

import edu.iastate.coms309.cyschedulebackend.persistence.repository.UserCredentialRepository;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.UserInformationRepository;
import edu.iastate.coms309.cyschedulebackend.persistence.requestModel.RegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            - getUserEmail/getUserID need to improve （2019-10-26）

     */

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserCredentialRepository userCredentialRepository;

    @Autowired
    UserInformationRepository userInformationRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Async
    @Transactional
    public void createUser(RegisterRequest user) {

        UserCredential userCredential = new UserCredential();
        UserInformation userInformation = new UserInformation();

        //update User Details
        userInformation.setUsername(user.getUsername());
        userInformation.setLastName(user.getLastName());
        userInformation.setFirstName(user.getFirstName());
        userInformation.setUserCredential(userCredential);
        userInformation.setRegisterTime(System.currentTimeMillis()/1000L);

        //Update User Credential
        userCredential.setEmail(user.getEmail());
        userCredential.setUserInformation(userInformation);
        userCredential.setPassword(passwordEncoder.encode(user.getPassword()));

        //save to database
        userCredentialRepository.save(userCredential);

        logger.info("New User with id :[" + userInformation.getUserID() + "] is created");
    }

    @Transactional
    @Cacheable(
            value = "false",
            unless = "#result == false"
    )
    public boolean existsByEmail(String email){ return userCredentialRepository.existsById(email);}

    @Transactional
    @Cacheable(
            value = "userid",
            key = "#email + '_id'"
    )
    public String getUserID(String email) { return userCredentialRepository.getOne(email).getUserInformation().getUserID(); }


    @Transactional
    @Cacheable(value = "email", key = "#userID + '_id'")
    public String getUserEmail(String userID) { return userInformationRepository.getOne(userID).getUserCredential().getEmail(); }

    @Transactional
    @Cacheable(value = "jwt_key", key = "#userID + '_jwt_key'")
    public String getJwtKey(String userID) {
        UserCredential userDetails = userCredentialRepository.getOne(userID);

        if(userDetails.getJwtKey() == null){
            userDetails.setJwtKey(UUID.randomUUID().toString());
            logger.info("New jwt Keys for [" + userID + "] is created");
            userCredentialRepository.save(userDetails);
        }
        return userDetails.getJwtKey();
    }

    @Transactional
    public Set<Permission> getAllPermission(String userID){
        return userCredentialRepository.getOne(getUserEmail(userID)).getPermissions();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (userCredentialRepository.existsById(email))
            return userCredentialRepository.getOne(email);
        else
            throw new UsernameNotFoundException("username " + email + " is not found");
    }

    public boolean checkPassword(String email, String password){
        return passwordEncoder.matches(password, userCredentialRepository.getOne(email).getPassword());
    }
}
