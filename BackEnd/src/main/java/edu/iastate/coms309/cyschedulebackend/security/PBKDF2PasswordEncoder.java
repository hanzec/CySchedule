package edu.iastate.coms309.cyschedulebackend.security;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Component;
import java.security.spec.InvalidKeySpecException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import edu.iastate.coms309.cyschedulebackend.Utils.ByteArrayUtil;
import edu.iastate.coms309.cyschedulebackend.Service.AccountService;

@Component
public class PBKDF2PasswordEncoder{

    @Value("${account.security.saltLength}")
    private Integer hashLength;

    @Value("${account.security.PBKDF2Count}")
    private Integer encryptCount;

    @Autowired
    AccountService accountService;

    public String encode(String password) {
        byte[] salt = generateSalt();
        return "PBKDF2." + ByteArrayUtil.ByteArrayToHex(salt) + "." + encode(password,salt);
    }

    public boolean matches(CharSequence charSequence, String s, Long userId) {
        String[] keyStorage = charSequence.toString().split("[.]");
        String hashedPassword = encode(keyStorage[3],accountService.getChallengeKeys(userId));
       return hashedPassword.equals(s);
    }

    public String encode(String s, byte[] password){
        KeySpec spec;
        SecretKeyFactory factory = null;
        spec = new PBEKeySpec(s.toCharArray(), password, encryptCount, 256);
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return "1000." + ByteArrayUtil.ByteArrayToHex(factory.generateSecret(spec).getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }

    public byte[] generateSalt(){
        SecureRandom random = null;
        byte[] randomTmp = new byte[hashLength];

        try {
            //User SecureRandom to avoid thread problem
            random = SecureRandom.getInstance("SHA1PRNG");
            random.nextBytes(randomTmp);
            return randomTmp;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
