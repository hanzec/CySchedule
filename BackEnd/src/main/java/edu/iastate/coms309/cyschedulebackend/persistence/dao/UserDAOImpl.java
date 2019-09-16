package edu.iastate.coms309.cyschedulebackend.persistence.dao;

import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class UserDAOImpl implements UserDAO{
    /*
        Maybe a improve point
            - Session could reuse for whole class (2019-9-13)
                * Problem may happened when this instance is closed without close session
            - After some operation my gen waste @ redis see updateEmail

     */
    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    private org.springframework.data.redis.core.RedisTemplate<String, Boolean> RedisTemplate;

    @Value("${account.security.saltLength}")
    private Integer hashLength;

    @Override
    public String createUser(String password, String firstName, String lastName, String email, String username) {
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setSalt(generateSalt());
        user.setRegisterTime(System.currentTimeMillis()/1000L);
        user.setPassword(generatePasswordPBKDF2(password,user.getSalt()));

        //update Redis Cached Value
        RedisTemplate.opsForValue().set("'user_id_"+ email,true);

        session.save(user);

        session.getTransaction().commit();

        session.close();

        return user.getUserID();
    }

    @Override
    public User findByUserID(String userId) {
        Session session = sessionFactory.openSession();

        User user = session.get(User.class,userId);

        session.close();

        return user;
    }

    @Override
    public User findByEmail(String email) {
        Session session = sessionFactory.openSession();

        User user = (User) session.createQuery("from User where User.email = :userEmail")
                                  .setParameter("userEmail",email).getSingleResult();

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
    @Cacheable(value = "userid", key = "'user_id_'+#email")
    public String gerUserID(String email) { return this.findByEmail(email).getUserID(); }


    @Override
    @Cacheable(value = "salt", key = "'user_salt_'+#email")
    public String getUserSalt(String email) { return this.findByEmail(email).getSalt(); }

    @Override
    @Cacheable(value = "isResisted", key = "'user_salt_'+#email")
    public boolean checkEmail(String email){
        Session session = sessionFactory.openSession();

        User user = (User) session.createQuery("from User where User.email = :userEmail")
                                  .setParameter("userEmail",email).uniqueResult();
        return user == null;
    }

    @Override
    @CachePut(value = "userid", key = "'user_id_'+#newEmail")
    public String updateEmail(String oldEmail, String newEmail) {
        Session session=sessionFactory.openSession();

        User user = findByEmail(oldEmail);
        user.setEmail(newEmail);

        session.beginTransaction();

        session.update(user);

        session.getTransaction().commit();

        session.close();

        return user.getUserID();
    }


    private String generateSalt(){
        SecureRandom random = null;
        byte[] randomTmp = new byte[hashLength];

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

    private String generatePasswordPBKDF2(String password, String salt){
        KeySpec spec;
        SecretKeyFactory factory = null;
        spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), 65536, 128);
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return Arrays.toString(factory.generateSecret(spec).getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }
}
