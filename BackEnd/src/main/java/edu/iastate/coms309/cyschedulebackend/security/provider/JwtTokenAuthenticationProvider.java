package edu.iastate.coms309.cyschedulebackend.security.provider;

import edu.iastate.coms309.cyschedulebackend.Service.UserTokenService;
import edu.iastate.coms309.cyschedulebackend.exception.auth.TokenAlreadyExpireException;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserToken;
import edu.iastate.coms309.cyschedulebackend.security.model.JwtAuthenticationToken;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.sql.Time;

public class JwtTokenAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserTokenService userTokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken credential = (JwtAuthenticationToken) authentication;
        UserToken token = userTokenService.getTokenObject((String) credential.getPrincipal());

        if (token.getExpireTime().before(new Time(System.currentTimeMillis())))
            throw new TokenAlreadyExpireException();

        if(token.getToken().equals())
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(JwtAuthenticationToken.class);
    }
}
