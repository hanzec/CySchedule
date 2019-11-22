package edu.iastate.coms309.cyschedulebackend.Service;


import edu.iastate.coms309.cyschedulebackend.exception.auth.EmailAlreadyExistException;
import edu.iastate.coms309.cyschedulebackend.exception.auth.PasswordNotMatchException;
import edu.iastate.coms309.cyschedulebackend.exception.user.UserAvatarNotFoundException;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.FileManagementService;
import edu.iastate.coms309.cyschedulebackend.persistence.model.FileObject;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.PushBuilder;
import java.io.File;
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
            - getUserEmail/getUserID need to improve （2019-10-26）(finished)
            - cache may not update when password is update (2019-10-27)
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
    public void createUser(RegisterRequest user) throws EmailAlreadyExistException{

        //There should not register with same email address
        if(this.existsByEmail(user.getEmail()))
            throw new EmailAlreadyExistException(user.getEmail());

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
        userCredential.setJwtKey(UUID.randomUUID().toString());
        userCredential.setPassword(passwordEncoder.encode(user.getPassword()));

        //save to database
        userCredentialRepository.save(userCredential);

        logger.info("New User with id :[" + userCredential.getUserID() + "] is created");
    }

//    public FileObject getAvatar(String id) throws UserAvatarNotFoundException {
////        if(userInformationRepository.isAvatarExist(id))
////            throw new UserAvatarNotFoundException(id);
//        logger.debug("New avatar request for user :[" + id + "]");
//        return userInformationRepository.getUserAvatar(id);
//    }

    @Async
    @Transactional
    public void updateAvatar(String id, FileObject fileObject){

        if(!userInformationRepository.existsById(id))
            throw new UsernameNotFoundException(id);

        UserInformation userInformation =  userInformationRepository.getOne(id);

        userInformation.setAvatar(fileObject);

        userInformationRepository.save(userInformation);
    }

    public void checkPassword(String email, String password) throws PasswordNotMatchException {
        if (!passwordEncoder.matches(password, userCredentialRepository.getOne(email).getPassword()))
            throw new PasswordNotMatchException(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserCredential userCredential;
        if (this.existsByEmail(email))
            userCredential = userCredentialRepository.getOne(email);
        else
            throw new UsernameNotFoundException("username " + email + " is not found");
        return userCredential;
    }

    @Transactional
    public void resetPassword(String userID, String newPassword, String oldPassword) throws PasswordNotMatchException {

        UserCredential userCredential = userCredentialRepository.getByUserID(userID);

        if(!passwordEncoder.matches(userCredential.getPassword(),oldPassword))
            throw new PasswordNotMatchException(userCredential.getEmail());

        userCredential.setPassword(passwordEncoder.encode(newPassword));

        userCredentialRepository.save(userCredential);
    }

    @Cacheable(
            value = "false",
            unless = "#result == false"
    )
    public boolean existsByEmail(String email){ return userCredentialRepository.existsById(email);}

    public UserInformation getUserInformation(String userID){ return userInformationRepository.getOne(userID); }

    @Async
    @Transactional
    public void updateUserInformation(UserInformation userInformation){ userInformationRepository.save(userInformation); }

    @Cacheable(value = "email", key = "#userID + '_id'")
    public String getUserEmail(String userID) { return userCredentialRepository.getUserEmailByUserID(userID); }

}
