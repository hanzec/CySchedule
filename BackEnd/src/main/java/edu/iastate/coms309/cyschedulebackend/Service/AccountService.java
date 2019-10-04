package edu.iastate.coms309.cyschedulebackend.Service;

import edu.iastate.coms309.cyschedulebackend.Utils.PasswordUtil;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.UserDAO;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.UserRoleDAO;
import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserRole;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

@Service
public class AccountService implements UserDAO {
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
    SessionFactory sessionFactory;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;


    @Override
    public String createUser(String password, String firstName, String lastName, String email, String username) {
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setSalt(passwordUtil.generateSalt());
        user.setRegisterTime(System.currentTimeMillis()/1000L);
        user.setPassword(passwordUtil.generatePasswordPBKDF2(password,user.getSalt()));

        //update Redis Cached Value
        session.save(user);

        session.getTransaction().commit();

        session.close();

        return user.getUserID();
    }


    @Override
    public User loadUserByEmail(String email){
        Session session = sessionFactory.openSession();

        User user;
        user = session.get(User.class,this.getUserID(email));

        session.close();

        return user;
    }

    @Override
    public User loadUserByUserID(String userId){
        Session session = sessionFactory.openSession();

        User user = session.get(User.class,userId);

        session.close();

        return user;
    }

    @Override
    public void deleteUser(String userID){
        Session session = sessionFactory.openSession();

        session.delete(session.get(User.class,userID));

        session.close();
    }

    @Override
    public String gerUserID(String email) {
        return null;
    }

    @Override
    public void changePassword(String userID, String password) {
        Session session = sessionFactory.openSession();
        User user = session.get(User.class,userID);

        user.setSalt(passwordUtil.generateSalt());
        user.setPassword(passwordUtil.generatePasswordPBKDF2(password,user.getSalt()));

        session.update(user);
        session.close();
    }

    @Override
    public boolean userExists(String email) {
        Session session = sessionFactory.openSession();

        User user = (User) session.createQuery("from User where User.email = :userEmail")
                .setParameter("userEmail",email).uniqueResult();
        return user == null;
    }

    @Cacheable(value = "userid", key = "'id_'+#email")
    public String getUserID(String email) { return this.loadUserByEmail(email).getUserID(); }

    @Override
    @Cacheable(value = "salt", key = "'salt_'+#email")
    public String getUserSalt(String email) { return this.loadUserByEmail(email).getSalt(); }

    @Override
    @Cacheable(value = "jwt_key", key = "'jwt_key_'+#userID")
    public String getJwtKey(String userID) {
        User user = loadUserByUserID(userID);

        if(user.getJwtKey() == null){
            Session session=sessionFactory.openSession();

            user.setJwtKey(passwordUtil.generateSalt());

            session.beginTransaction();

            session.update(user);

            session.getTransaction().commit();

            session.close();
        }

        return user.getJwtKey();
    }

    @Override
    @Cacheable(value = "password", key = "'user_pass_'+#email")
    public String getPasswordByEmail(String email) { return this.loadUserByEmail(email).getPassword(); }

    @Override
    public void setUserRole(String userID, UserRole role) {
        User user = loadUserByUserID(userID);
        Session session=sessionFactory.openSession();

        user.getUserRoles().add(role);

        session.beginTransaction();

        session.update(user);

        session.getTransaction().commit();

        session.close();
    }

    @Override
    @CachePut(value = "userid", key = "'user_id_'+#newEmail")
    public String updateEmail(String oldEmail, String newEmail) {
        Session session=sessionFactory.openSession();

        User user = loadUserByEmail(oldEmail);
        user.setEmail(newEmail);

        session.beginTransaction();

        session.update(user);

        session.getTransaction().commit();

        session.close();

        return user.getUserID();
    }
}
