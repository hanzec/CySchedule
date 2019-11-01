package edu.iastate.coms309.cyschedulebackend.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import com.auth0.jwt.JWT;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import edu.iastate.coms309.cyschedulebackend.security.model.AuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenFilter extends AbstractPreAuthenticatedProcessingFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public TokenFilter(AuthenticationManager authenticationManager){
        super();
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
        return ((DecodedJWT)JWT.decode(httpServletRequest.getHeader("Authorization"))).getId();
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return JWT.decode(httpServletRequest.getHeader("Authorization"));
    }
}
