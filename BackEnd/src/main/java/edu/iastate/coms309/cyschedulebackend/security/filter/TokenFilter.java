package edu.iastate.coms309.cyschedulebackend.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import com.auth0.jwt.JWT;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.Assert;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class TokenFilter extends GenericFilterBean implements ApplicationEventPublisherAware {
    private String authenticatedPrincipal = null;
    private ApplicationEventPublisher eventPublisher = null;
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    private AuthenticationManager authenticationManager = null;
    private boolean continueFilterChainOnUnsuccessfulAuthentication = true;
    private boolean checkForPrincipalChanges;
    private boolean invalidateSessionOnPrincipalChange = true;
    private AuthenticationSuccessHandler authenticationSuccessHandler = null;
    private AuthenticationFailureHandler authenticationFailureHandler = null;

    public TokenFilter(AuthenticationManager authenticationManager){
        this.setAuthenticationManager(authenticationManager);
    }

    public void afterPropertiesSet() {
        try {
            super.afterPropertiesSet();
        } catch (ServletException var2) {
            throw new RuntimeException(var2);
        }
        Assert.notNull(this.authenticationManager, "An AuthenticationManager must be set");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (((HttpServletRequest) request).getHeader("Authorization") != null)
            chain.doFilter(request, response);
        else
            this.authenticatedPrincipal = JWT.decode(((HttpServletRequest) request).getHeader("Authorization")).getId();

        if (this.requiresAuthentication((HttpServletRequest)request)) {
            this.doAuthenticate((HttpServletRequest)request, (HttpServletResponse)response);
        }


    }

    protected boolean principalChanged(HttpServletRequest request, Authentication currentAuthentication) {
        Object principal = this.authenticatedPrincipal;

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

    private void doAuthenticate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Object principal = this.authenticatedPrincipal;
        Object credentials = request.getHeader("Authorization");

        if (principal == null) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("No pre-authenticated principal found in request");
            }

        } else {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("preAuthenticatedPrincipal = " + principal + ", trying to authenticate");
            }

            try {
                AuthenticationToken authRequest = new AuthenticationToken(principal, credentials, request.getRequestURI());
                authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
                Authentication authResult = this.authenticationManager.authenticate(authRequest);
                this.successfulAuthentication(request, response, authResult);
            } catch (AuthenticationException var7) {
                this.unsuccessfulAuthentication(request, response, var7);
                if (!this.continueFilterChainOnUnsuccessfulAuthentication) {
                    throw var7;
                }
            }

        }
    }

    private boolean requiresAuthentication(HttpServletRequest request) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            return true;
        } else if (!this.checkForPrincipalChanges) {
            return false;
        } else if (!this.principalChanged(request, currentUser)) {
            return false;
        } else {
            this.logger.debug("Pre-authenticated principal has changed and will be reauthenticated");
            if (this.invalidateSessionOnPrincipalChange) {
                SecurityContextHolder.clearContext();
                HttpSession session = request.getSession(false);
                if (session != null) {
                    this.logger.debug("Invalidating existing session");
                    session.invalidate();
                    request.getSession();
                }
            }

            return true;
        }
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException, ServletException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Authentication success: " + authResult);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);
        if (this.eventPublisher != null) {
            this.eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
        }

        if (this.authenticationSuccessHandler != null) {
            this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, authResult);
        }

    }

    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Cleared security context due to exception", failed);
        }

        request.setAttribute("SPRING_SECURITY_LAST_EXCEPTION", failed);
        if (this.authenticationFailureHandler != null) {
            this.authenticationFailureHandler.onAuthenticationFailure(request, response, failed);
        }

    }

    public void setApplicationEventPublisher(ApplicationEventPublisher anApplicationEventPublisher) {
        this.eventPublisher = anApplicationEventPublisher;
    }

    public void setAuthenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        Assert.notNull(authenticationDetailsSource, "AuthenticationDetailsSource required");
        this.authenticationDetailsSource = authenticationDetailsSource;
    }

    protected AuthenticationDetailsSource<HttpServletRequest, ?> getAuthenticationDetailsSource() {
        return this.authenticationDetailsSource;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setContinueFilterChainOnUnsuccessfulAuthentication(boolean shouldContinue) {
        this.continueFilterChainOnUnsuccessfulAuthentication = shouldContinue;
    }

    public void setCheckForPrincipalChanges(boolean checkForPrincipalChanges) {
        this.checkForPrincipalChanges = checkForPrincipalChanges;
    }

    public void setInvalidateSessionOnPrincipalChange(boolean invalidateSessionOnPrincipalChange) {
        this.invalidateSessionOnPrincipalChange = invalidateSessionOnPrincipalChange;
    }

    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }
}
