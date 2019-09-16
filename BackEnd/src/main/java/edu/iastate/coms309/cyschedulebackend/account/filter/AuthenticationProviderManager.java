package edu.iastate.coms309.cyschedulebackend.account.filter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class AuthenticationProviderManager implements AuthenticationManager, MessageSourceAware, InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setMessageSource(MessageSource messageSource) {

    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }
}
