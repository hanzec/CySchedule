package edu.iastate.coms309.cyschedulebackend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PBKDF2PasswordEncoder implements PasswordEncoder {
    @Value("${account.security.saltLength}")
    private Integer hashLength;

    @Autowired
    

    @Override
    public String encode(CharSequence charSequence) {
        return null;
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return false;
    }
}
