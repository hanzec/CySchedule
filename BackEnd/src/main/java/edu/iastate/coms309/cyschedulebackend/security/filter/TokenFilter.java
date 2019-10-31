package edu.iastate.coms309.cyschedulebackend.security.filter;

import org.slf4j.Logger;
import com.auth0.jwt.JWT;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
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

public class TokenFilter extends GenericFilterBean {

    private AuthenticationManager authenticationManager = null;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();


    public TokenFilter(AuthenticationManager authenticationManager){
        super();
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Checking secure context token: " + SecurityContextHolder.getContext().getAuthentication());
        }

        if (this.requiresAuthentication((HttpServletRequest)request)) {
            this.doAuthenticate((HttpServletRequest)request);
        }

        chain.doFilter(request, response);
    }

    protected boolean principalChanged(HttpServletRequest request, Authentication currentAuthentication) {
        Object principal = JWT.decode(request.getHeader("Authorization")).getId();
        if (principal != null && currentAuthentication.getName().equals(principal)) {
            return false;
        } else if (principal != null && principal.equals(currentAuthentication.getPrincipal())) {
            return false;
        } else {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Pre-authenticated principal has changed to " + principal + " and will be reauthenticated");
            }
            return true;
        }
    }

    private void doAuthenticate(HttpServletRequest request){
        Object principal = JWT.decode(request.getHeader("Authorization")).getId();
        Object credentials = JWT.decode(request.getHeader("Authorization"));
        if (principal == null) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("No pre-authenticated principal found in request");
            }

        } else {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("preAuthenticatedPrincipal = " + principal + ", trying to authenticate");
            }

                AuthenticationToken authRequest = new AuthenticationToken(principal, credentials,request.getRequestURI());
                authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
                Authentication authResult = this.authenticationManager.authenticate(authRequest);
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Authentication success: " + authResult);
                }
                SecurityContextHolder.getContext().setAuthentication(authResult);
        }
    }

    private boolean requiresAuthentication(HttpServletRequest request) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            return true;
        } else if (!this.principalChanged(request, currentUser)) {
            return false;
        } else {
            this.logger.debug("Pre-authenticated principal has changed and will be reauthenticated");
            return true;
        }
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
