package edu.iastate.coms309.cyschedulebackend.configuration;

import edu.iastate.coms309.cyschedulebackend.security.PBKDF2PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@Configuration
public class SpringSecurityConfiguration {
    @Bean
    public PBKDF2PasswordEncoder passwordEncoder() {
        return new PBKDF2PasswordEncoder();
    }
}
